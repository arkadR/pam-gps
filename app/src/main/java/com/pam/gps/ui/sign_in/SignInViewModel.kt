package com.pam.gps.ui.sign_in

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.pam.gps.utils.SingleLiveEvent
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import kotlinx.coroutines.tasks.await

class SignInViewModel : ViewModel() {

  val authStatus: SingleLiveEvent<Unit> = SingleLiveEvent()
  private val _loading = MutableLiveData<Boolean>(false)
  val loading: LiveData<Boolean> = _loading

  fun authenticate(username: String, password: String, handler: CoroutineExceptionHandler) {
    _loading.value = true
    (viewModelScope + handlerDecorator(handler) + Dispatchers.Main).launch {
      FirebaseAuth
        .getInstance()
        .signInWithEmailAndPassword(username, password)
        .await()
      authStatus.call()
    }
  }

  private fun handlerDecorator(handler: CoroutineExceptionHandler): CoroutineExceptionHandler {
    return CoroutineExceptionHandler { context, throwable ->
      _loading.value = false
      handler.handleException(context, throwable)
    }
  }
}
