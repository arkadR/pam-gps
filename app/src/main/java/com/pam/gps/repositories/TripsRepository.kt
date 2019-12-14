package com.pam.gps.repositories

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import com.pam.gps.commandObjects.TripCommand
import com.pam.gps.commandObjects.TripDetailsCommand
import com.pam.gps.extensions.asFlow
import com.pam.gps.model.Coordinate
import com.pam.gps.model.Trip
import com.pam.gps.model.TripDetails
import com.pam.gps.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.tasks.await
import timber.log.Timber

open class TripsRepository {

  private val db = Firebase.firestore
  private val userId by lazyOf(
    FirebaseAuth.getInstance().currentUser?.uid ?: throw RuntimeException("userId not available")
  )

  companion object {
    const val tripsDetails = "trips_details"
    const val users = "users"
    const val trips = "trips"
    const val tripCoordinates = "coordinates"
    const val tripPictures = "pictures"
    const val tripFinished = "finished"
    const val owner = "owner"
    const val access = "access"
  }

  fun getTrips(): Flow<List<Trip>> {
    return dbCurrentUserTripsCollection
      .asFlow()
      .map { query ->
        query?.documents?.mapNotNull { document ->
          document.toObject<TripCommand>()?.toTrip(document.id)
        }
          ?: throw RuntimeException("Query returned null") //TODO[ME] Don't know what's exactly going on here and why would it fail
      }
      .map { list -> list.filter { trip -> trip.finished } }
      .flowOn(Dispatchers.IO)
  }

  fun getTripDetailsForTrip(trip: Trip): Flow<TripDetails?> {
    return dbTripsDetailsCollection
      .document(trip.details)
      .asFlow()
      .map { Timber.d(it.toString()); it?.toObject<TripDetailsCommand>()?.toTripDetails(it.id) }
  }

  fun getCurrentTrip(): Flow<List<Trip>> {
    return dbCurrentUserTripsCollection
      .whereEqualTo(tripFinished, false)
      .asFlow()
      .mapNotNull { it?.toObjects<Trip>() }

  }

  fun getCurrentTripDetails(): Flow<List<TripDetails>> {
    return dbTripsDetailsCollection
      .whereEqualTo(tripFinished, false)
      .whereArrayContains(access, User(userId, owner))
      .asFlow()
      .mapNotNull { it?.toObjects<TripDetails>() }
  }

  suspend fun createTrip(): Pair<Trip, TripDetails> {
    val tripRef = dbCurrentUserTripsCollection.document()
    val tripDetailsRef = dbTripsDetailsCollection.document()

    val tripCommand = TripCommand(details = tripDetailsRef.id, id = tripRef.id)
    val tripDetailsCommand = TripDetailsCommand(
      id = tripDetailsRef.id,
      access = listOf(User(userId, "owner"))
    )

    db.runBatch { batch ->
      batch.set(tripDetailsRef, tripDetailsCommand)
      batch.set(tripRef, tripCommand)
    }.await()
    return Pair(tripCommand.toTrip(tripRef.id), tripDetailsCommand.toTripDetails(tripDetailsRef.id))
  }

  suspend fun finishTrip(trip: Trip) {
    dbCurrentUserTripsCollection
      .document(trip.id)
      .update(tripFinished, true)
      .await()
  }

  suspend fun addCoordinates(tripDetails: TripDetails, coordinates: Array<Coordinate>) =
    addArrayDataToDetails(tripDetails, tripCoordinates, coordinates)

  suspend fun addPhotoToTripDetails(tripDetails: TripDetails, photoPaths: Array<String>) =
    addArrayDataToDetails(tripDetails, tripPictures, photoPaths)

  private suspend fun <T> addArrayDataToDetails(tripDetails: TripDetails, field: String, data: Array<T>) {
    dbTripsDetailsCollection.document(tripDetails.id)
      .update(field, FieldValue.arrayUnion(*data))
      .await()
  }

  private val dbCurrentUserTripsCollection : CollectionReference
    get() = this.db.collection(users)
        .document(userId)
        .collection(trips)

  private val dbTripsDetailsCollection : CollectionReference
    get() = this.db
      .collection(tripsDetails)
}