package com.pam.gps.extensions

fun Double.withDecimalPlaces(n: Int) : String = "%.${n}f".format(this)