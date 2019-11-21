package com.pam.gps.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference

data class Trip(
  val title: String = "",
  val date: Timestamp? = null,
  val details: DocumentReference? = null
) {
}