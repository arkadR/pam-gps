package com.pam.gps.repositories

import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.pam.gps.extensions.asFlow
import com.pam.gps.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import timber.log.Timber

open class TripsRepository {

  private val db = Firebase.firestore
  private val userId by lazyOf(
    FirebaseAuth.getInstance().currentUser?.uid ?: throw RuntimeException("userId not available")
  )

  companion object {
    const val tripsDetails = "trips_details"
    const val collection_users = "users"
    const val trips = "trips"
    const val collection_current_trips = "current_trips"
    const val tripPictures = "pictures"
  }

  fun getTrips(): Flow<List<Trip>> {
    return dbCurrentUserTripsCollection
      .asFlow()
      .map { query ->
        query?.toObjects(Trip::class.java)
          ?: throw RuntimeException("Query returned null") //TODO[ME] Don't know what's exactly going on here and why would it fail
      }
      .flowOn(Dispatchers.IO)
  }

  fun getTripDetailsForTrip(trip: Trip): Flow<TripDetails?> {
    return dbTripsDetailsCollection
      .document(trip.details)
      .asFlow()
      .map { Timber.d(it.toString()); it?.toObject<TripDetails>() }
  }

  fun getCurrentTrip(): Flow<CurrentTrip?> {
    return db
      .collection(collection_current_trips)
      .document(userId)
      .asFlow()
      .map { it?.toObject<CurrentTrip>() }
  }

  suspend fun getCurrentTripSnapshot(): CurrentTrip? {
    return db
      .collection(collection_current_trips)
      .document(userId)
      .get()
      .await()
      .toObject<CurrentTrip>()
  }

  suspend fun createTrip(): CurrentTrip {
    val ts = Timestamp.now()

    val currentTripRef = db.collection(collection_current_trips).document(userId)

    val trip = Trip(date = ts)
    val tripDetails = TripDetails(
      date = ts,
      access = listOf(User(userId, "owner"))
    )
    val currentTrip = CurrentTrip(userId, trip, tripDetails)

    currentTripRef.set(currentTrip).await()
    return currentTrip
  }

  suspend fun addCoordinates(coordinates: Array<Coordinate>) {
    addArrayDataToCurrentTripDetails("coordinates", coordinates)
  }

  suspend fun addPhotoToTripDetails(tripDetails: TripDetails, photoPaths: Array<String>) {
    db.collection(tripsDetails)
      .document(tripDetails.id)
      .update(tripPictures, FieldValue.arrayUnion(*photoPaths))
      .await()
  }

  private suspend fun <T> addArrayDataToCurrentTripDetails(field: String, data: Array<T>) {
    db.collection(collection_current_trips)
      .document(userId)
      .update("tripDetails.$field", FieldValue.arrayUnion(*data))
      .await()
  }

  suspend fun addPhotoToCurrentTrip(photoPaths: Array<String>) {
    addArrayDataToCurrentTripDetails("pictures", photoPaths)
  }

  private val dbCurrentUserTripsCollection: CollectionReference
    get() = this.db.collection(collection_users)
      .document(userId)
      .collection(trips)

  private val dbTripsDetailsCollection: CollectionReference
    get() = this.db
      .collection(tripsDetails)
}