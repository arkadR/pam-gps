package com.pam.gps.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager


fun hideKeyboard(context: Context, view: View): Unit {
  val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
  imm.hideSoftInputFromWindow(view.windowToken, 0)
}