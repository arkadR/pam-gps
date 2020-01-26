package com.pam.gps.model

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth

class FirebaseAuthProviderImpl : AuthProvider {

  private val firebaseAuth by lazy { FirebaseAuth.getInstance() }

  override fun signInWithEmailAndPassword(username: String, password: String): Task<AuthResult> {
    return firebaseAuth.signInWithEmailAndPassword(username, password)
  }

  override fun signOut() {
    firebaseAuth.signOut()
  }
}