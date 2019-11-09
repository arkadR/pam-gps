package com.pam.gps.extensions

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect

fun <T> Flow<T>.asLiveData() : LiveData<T> {
  return liveData {
    this@asLiveData.collect {
      emit(it)
    }
  }
}