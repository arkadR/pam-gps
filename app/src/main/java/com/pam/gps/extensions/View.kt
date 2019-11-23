package com.pam.gps.extensions

import android.view.View
import android.widget.EditText
import com.google.android.material.textfield.TextInputLayout

fun EditText.withValidate(
  layout: TextInputLayout,
  errorText: String,
  constraint: () -> Boolean
) {
  onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
    if (!hasFocus) {
      if (!constraint()) {
        layout.error = errorText
      } else layout.error = null
    }
  }
}

fun View.setOnClickListenerRequirements(
  vararg requirements: Pair<View, () -> Boolean>,
  listener: View.OnClickListener
) {
  setOnClickListener {
    requirements.forEach { (view, requirement) ->
      view.onFocusChangeListener.onFocusChange(view, false)
      if (!requirement()) {
        view.requestFocus()
        return@setOnClickListener
      }
    }
    listener.onClick(this)
  }
}

