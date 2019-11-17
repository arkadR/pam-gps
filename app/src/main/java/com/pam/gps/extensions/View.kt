package com.pam.gps.extensions

import android.view.View
import android.widget.EditText
import com.google.android.material.textfield.TextInputLayout

fun EditText.withValidate(
  layout: TextInputLayout,
  errorText: String,
  constraint: (String) -> Boolean
) {
  onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
    if (!hasFocus) {
      if (!constraint(text.toString())) {
        layout.error = errorText
      }
    } else layout.error = null
  }
}

fun EditText.withValidateEmail(layout: TextInputLayout, errorText: String) {
  withValidate(layout, errorText) {
    it.matches(
      "[^( \t\n\r)]+[@][^( \t\n\r)]+[.][^( \t\n\r)]+".toRegex()
    )
  }
}

fun EditText.validate(constraint: (String) -> Boolean) =
  constraint(text.toString())

fun EditText.validateEmail(): Boolean =
  text.toString().matches("[^( \t\n\r)]+[@][^( \t\n\r)]+[.][^( \t\n\r)]+".toRegex())


