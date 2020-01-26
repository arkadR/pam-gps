package com.pam.gps.model

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult


interface AuthProvider {
  fun signInWithEmailAndPassword(username: String, password: String): Task<AuthResult>
  fun signOut()
}