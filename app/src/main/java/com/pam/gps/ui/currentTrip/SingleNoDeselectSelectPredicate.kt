package com.pam.gps.ui.currentTrip

import androidx.recyclerview.selection.SelectionTracker

object SingleNoDeselectSelectPredicate : SelectionTracker.SelectionPredicate<String>() {
  override fun canSelectMultiple(): Boolean = false

  override fun canSetStateForKey(key: String, nextState: Boolean): Boolean = nextState

  override fun canSetStateAtPosition(position: Int, nextState: Boolean): Boolean = true
}