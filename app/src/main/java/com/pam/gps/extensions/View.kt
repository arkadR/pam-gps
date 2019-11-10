package com.pam.gps.extensions

import android.view.View
import com.google.android.gms.common.internal.Asserts.checkMainThread
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

fun View.clicks(): Flow<Unit> =
  callbackFlow {
    val listener = View.OnClickListener {
      offer(Unit)
    }
    setOnClickListener(listener)
    awaitClose { setOnClickListener(null) }
  }