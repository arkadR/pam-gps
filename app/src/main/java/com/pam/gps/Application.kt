package com.pam.gps

import timber.log.Timber

class Application : android.app.Application() {
  override fun onCreate() {
    super.onCreate()
    Timber.plant(Timber.DebugTree())
  }
}