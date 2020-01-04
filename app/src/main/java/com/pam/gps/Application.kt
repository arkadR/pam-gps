package com.pam.gps

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.pam.gps.utils.LocalPhotoCache
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.qualifier.named
import org.koin.dsl.module
import timber.log.Timber

class Application : android.app.Application() {
  override fun onCreate() {
    super.onCreate()
    Timber.plant(Timber.DebugTree())
    val appModule = module {
      single(named("local_photo_cache_prefs")) { provideLocalPhotoCachePreferences(androidApplication()) }
      single { LocalPhotoCache(provideLocalPhotoCachePreferences(androidApplication())) }
    }
    startKoin {
      androidContext(this@Application)
      modules(appModule)
    }
  }

  private fun provideLocalPhotoCachePreferences(app: Application): SharedPreferences =
    app.getSharedPreferences("com.pam.gps.LOCAL_PHOTO_CACHE", Context.MODE_PRIVATE)
}