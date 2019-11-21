package com.pam.gps.model

import android.os.Parcelable
import com.google.firebase.Timestamp
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Trip(
  val title: String = "",
  val date: Timestamp? = null,
  val details: String = ""
) : Parcelable