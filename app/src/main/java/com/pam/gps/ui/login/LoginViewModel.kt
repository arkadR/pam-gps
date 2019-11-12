package com.pam.gps.ui.login

import androidx.lifecycle.*
import com.google.firebase.auth.FirebaseAuth
import com.pam.gps.utils.SingleLiveEvent
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import timber.log.Timber

class LoginViewModel : ViewModel() {

  val authStatus: SingleLiveEvent<Unit> = SingleLiveEvent()

  fun authenticate(username: String, password: String, handler: CoroutineExceptionHandler) {
    (viewModelScope + handler + Dispatchers.Main).launch {
      FirebaseAuth
        .getInstance()
        .signInWithEmailAndPassword(username, password)
        .await()
        authStatus.call()
    }
  }
}
