package com.pam.gps.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.pam.gps.utils.SingleLiveEvent
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import kotlinx.coroutines.tasks.await

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
