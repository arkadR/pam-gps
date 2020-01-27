package com.pam.gps.repositories

import android.net.Uri
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.pam.gps.extensions.asFlow
import com.pam.gps.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import timber.log.Timber

open class TripsRepository {

  private val db = Firebase.firestore
  private val userId by lazyOf(
    FirebaseAuth.getInstance().currentUser?.uid ?: throw RuntimeException("userId not available")
  )

  private val photosRepository = PhotosRepository()

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

  suspend fun getTripByTripDetailsId(tripDetailsId: String): Trip {
    return dbCurrentUserTripsCollection
      .whereEqualTo("details", tripDetailsId)
      .get()
      .await()
      .first()
      .toObject()
  }

  fun getTripDetailsById(tripDetailsId: String): Flow<TripDetails?> {
    return dbTripsDetailsCollection
      .document(tripDetailsId)
      .asFlow()
      .map { it?.toObject<TripDetails>() }
  }

  fun getCurrentTrip(): Flow<CurrentTrip?> {
    return dbCurrentUserCurrentTripReference
      .asFlow()
      .map { it?.toObject<CurrentTrip>() }
  }

  suspend fun getCurrentTripSnapshot(): CurrentTrip? {
    return dbCurrentUserCurrentTripReference
      .get()
      .await()
      .toObject<CurrentTrip>()
  }

  suspend fun createTrip(): CurrentTrip {
    val ts = Timestamp.now()

    val currentTripRef = dbCurrentUserCurrentTripReference
    val tripDetailsRef = dbTripsDetailsCollection.document()

    val trip = Trip(date = ts)
    val tripDetails = TripDetails(
      id = tripDetailsRef.id,
      date = ts,
      access = listOf(User(userId, "owner"))
    )
    val currentTrip = CurrentTrip(userId, trip, tripDetails)

    val batch = Firebase.firestore.batch()

    batch.set(currentTripRef, currentTrip)
    batch.set(tripDetailsRef, tripDetails)

    batch.commit().await()
    Timber.d("creating trip $currentTrip")
    return currentTrip
  }

  suspend fun finishTrip(title: String, thumbnailPath: String) {
    val (_, trip, tripDetails) = getCurrentTripSnapshot()
      ?: throw RuntimeException("Current trip returned null")
    if (trip == null || tripDetails == null)
      throw RuntimeException("trip = $trip, tripDetails = $tripDetails while finishing trip")
    val tripDetailsReference = dbTripsDetailsCollection.document(tripDetails.id)

    val tripReference = dbCurrentUserTripsCollection.document()
    val tripWithIds = trip.copy(
      id = tripReference.id,
      details = tripDetails.id,
      title = title,
      picture = thumbnailPath
    )
    try {
      db.runBatch { batch ->
        batch.set(tripReference, tripWithIds)
        batch.set(tripDetailsReference, tripDetails.copy(title = title))
        batch.delete(dbCurrentUserCurrentTripReference)
      }.await()
    } catch (ex: FirebaseFirestoreException) {
      Timber.e(ex)
    }
  }


  suspend fun discardTrip() {
    val (_, trip, tripDetails) = getCurrentTripSnapshot()
      ?: throw RuntimeException("Current trip returned null")
    if (trip == null || tripDetails == null)
      throw RuntimeException("trip = $trip, tripDetails = $tripDetails while finishing trip")
    val tripDetailsReference = dbTripsDetailsCollection.document(tripDetails.id)

    db.runBatch { batch ->
      //TODO[AR]: Remove photos from storage?
      batch.delete(tripDetailsReference)
      batch.delete(dbCurrentUserCurrentTripReference)
    }.await()
  }

  fun getAllTripsDetails(): Flow<List<TripDetails>> {
    return dbTripsDetailsCollection
      .asFlow()
      .map { query -> query?.toObjects(TripDetails::class.java) }
      .filterNotNull()
  }

  suspend fun addCoordinates(coordinates: Array<Coordinate>) {
    addArrayDataToCurrentTripDetails("coordinates", coordinates)
  }

//  suspend fun addPhotoToTripDetails(tripDetails: TripDetails, photoPaths: Array<String>) {
//    db.collection(tripsDetails)
//      .document(tripDetails.id)
//      .update(tripPictures, FieldValue.arrayUnion(*photoPaths))
//      .await()
//  }

  private suspend fun <T> addArrayDataToCurrentTripDetails(field: String, data: Array<T>) {
    dbCurrentUserCurrentTripReference
      .update("tripDetails.$field", FieldValue.arrayUnion(*data))
      .await()
  }

//  suspend fun addPhotoToCurrentTrip(photoPaths: Array<String>) {
//    addArrayDataToCurrentTripDetails("pictures", photoPaths)
//  }


  suspend fun addPicturesToCurrentTrip(pictures: Array<Picture>) {
    val currentTrip = getCurrentTripSnapshot()
      ?: throw RuntimeException("Attempting to post photo without current trip present")
    pictures.forEach { pic ->
      val ref = photosRepository.addPhotoToTrip(currentTrip, Uri.parse(pic.uri))
      pic.storageRef = ref
    }
    addArrayDataToCurrentTripDetails("pictures", pictures)
  }

  private val dbCurrentUserTripsCollection: CollectionReference
    get() = this.db.collection(collection_users)
      .document(userId)
      .collection(trips)

  private val dbTripsDetailsCollection: CollectionReference
    get() = this.db
      .collection(tripsDetails)

  private val dbCurrentUserCurrentTripReference: DocumentReference
    get() = this.db
      .collection(collection_current_trips)
      .document(userId)
}