package com.pam.gps.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint

data class Coordinate(val geoPoint: GeoPoint, val timestamp: Timestamp)