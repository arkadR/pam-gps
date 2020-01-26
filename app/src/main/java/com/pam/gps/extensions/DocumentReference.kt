package com.pam.gps.extensions

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

fun DocumentReference.asFlow(): Flow<DocumentSnapshot?> {
  return callbackFlow {
    val onSnapShotListener = EventListener<DocumentSnapshot> { snapshot, exception ->
      if (exception != null) cancel(CancellationException("query: ${this@asFlow}", exception))
      offer(snapshot)
    }
    val registration = this@asFlow.addSnapshotListener(onSnapShotListener)
    FirebaseAuth.getInstance().addAuthStateListener {
      if (it.currentUser == null) {
        registration.remove()
        close()
      }
    }
    awaitClose { registration.remove() }
  }
}