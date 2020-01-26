package com.pam.gps.extensions

import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

fun Query.asFlow(): Flow<QuerySnapshot?> {
  return callbackFlow {
    val onSnapShotListener = EventListener<QuerySnapshot> { snapshot, exception ->
      if (exception != null) cancel(CancellationException("QUERY: $this@asFlow", exception))
      offer(snapshot)
    }
    val registration = this@asFlow.addSnapshotListener(onSnapShotListener)
    awaitClose { registration.remove() }
  }
}