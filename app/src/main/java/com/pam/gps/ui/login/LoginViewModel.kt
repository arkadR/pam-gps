package com.pam.gps.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import timber.log.Timber

class LoginViewModel : ViewModel() {
  enum class AuthStatus {
    AUTHENTICATED, NOT_AUTHENTICATED, INVALID_AUTHENTICATION
  }

  @ExperimentalCoroutinesApi
  val authStatus: LiveData<AuthStatus> = callbackFlow {
    val listener = FirebaseAuth.AuthStateListener {
      val user = it.currentUser
      Timber.d("user = $user")
      if (user == null) {
        offer(AuthStatus.NOT_AUTHENTICATED)
      }
      else offer(AuthStatus.AUTHENTICATED)
    }
    FirebaseAuth.getInstance().addAuthStateListener(listener)
    awaitClose { FirebaseAuth.getInstance().removeAuthStateListener(listener) }
  }.asLiveData()

  fun authenticate(username: String, password: String) {
    viewModelScope.launch {
      Timber.d("Signing in with username = $username, password = $password")
      FirebaseAuth
        .getInstance()
        .signInWithEmailAndPassword(username, password)
        .await()
    }
  }
}