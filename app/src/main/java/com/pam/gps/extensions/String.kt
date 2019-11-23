package com.pam.gps.extensions

fun String.validateEmail(): Boolean =
  this.matches("[^( \t\n\r)]+[@][^( \t\n\r)]+[.][^( \t\n\r)]+".toRegex())
