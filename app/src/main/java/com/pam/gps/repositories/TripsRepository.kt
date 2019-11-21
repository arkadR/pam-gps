package com.pam.gps.repositories

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import com.pam.gps.extensions.asFlow
import com.pam.gps.model.Coordinate
import com.pam.gps.model.Trip
import com.pam.gps.model.TripDetails
import com.pam.gps.model.User
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.tasks.asDeferred
import timber.log.Timber

open class TripsRepository {

  private val db = Firebase.firestore

  companion object {
    const val tripsDetails = "trips_details"
    const val users = "users"
    const val trips = "trips"
    const val tripCoordinates = "coordinates"
    const val tripFinished = "finished"
    const val owner = "owner"
    const val access = "access"
  }

  fun getTrips(userId: String): Flow<List<Trip>> {
    return db
      .collection(users)
      .document(userId)
      .collection(trips)
      .asFlow()
      .map { it!!.toObjects<Trip>() } //TODO Maybe it does return null e.g. when there is no connection?
      .flowOn(Dispatchers.IO)
  }

  //returns null when document doesn't exist
  fun getTripDetails(tripPath: String): Flow<TripDetails?> {
    return db
      .document(tripPath)
      .asFlow()
      .map { Timber.d(it.toString()); it?.toObject<TripDetails>() }
  }

  fun getTripDetails(tripDetailsReference: DocumentReference): Flow<TripDetails?> {
    return tripDetailsReference.asFlow().mapNotNull { it?.toObject<TripDetails>() }
  }


  fun getCurrentTripDetails(userId: String): Flow<List<TripDetails>> {
    return db
      .collection(tripsDetails)
      .whereEqualTo(tripFinished, false)
      .whereArrayContains(access, User(userId, owner))
      .asFlow()
      .mapNotNull { it?.toObjects<TripDetails>() }
  }

  fun createTripDetailsAsync(userId: String): Deferred<DocumentReference> {
    return db.collection(tripsDetails).add(
      TripDetails(
        access = listOf(User(userId, owner))
      )
    ).asDeferred()
  }

  fun addCoordinatesAsync(
    documentReference: DocumentReference,
    coordinates: List<Coordinate>
  ): Deferred<Void> {
    return documentReference.update(tripCoordinates, FieldValue.arrayUnion(coordinates))
      .asDeferred()
  }

  fun addCoordinatesAsync(tripDetailsId: String, coordinates: List<Coordinate>): Deferred<Void> {
    return addCoordinatesAsync(
      db.collection(trips).document(tripDetailsId),
      coordinates
    )
  }

  fun saveTripAsync(
    userId: String,
    tripDetails: TripDetails,
    tripDetailsReference: DocumentReference
  ): Deferred<Void> {
    val tripRef = db.collection(users).document(userId).collection(trips).document()
    val trip = Trip(tripDetails.title, tripDetails.date, tripDetailsReference.path)
    return db.runBatch { batch ->
      batch.update(tripDetailsReference, tripFinished, true)
      batch.set(tripRef, trip)
    }.asDeferred()
  }
}