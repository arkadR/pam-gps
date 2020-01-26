package com.pam.gps

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.pam.gps.model.AuthProvider
import com.pam.gps.model.FirebaseAuthProviderImpl
import com.pam.gps.utils.LocalPhotoCache
import com.pam.gps.utils.PhotoLocationCache
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
      single(named("local_photo_cache_prefs")) {
        provideLocalPhotoCachePreferences(
          androidApplication()
        )
      }
      single(named("location_cache_prefs")) { provideLocationCachePreferences(androidApplication()) }
      single { LocalPhotoCache() }
      single { PhotoLocationCache() }
      single<AuthProvider> { FirebaseAuthProviderImpl() }
    }
    startKoin {
      androidContext(this@Application)
      modules(appModule)
    }
  }

  private fun provideLocalPhotoCachePreferences(app: Application): SharedPreferences =
    app.getSharedPreferences("com.pam.gps.LOCAL_PHOTO_CACHE", Context.MODE_PRIVATE)

  private fun provideLocationCachePreferences(app: Application): SharedPreferences =
    app.getSharedPreferences("com.pam.gps.LOCATION_CACHE", Context.MODE_PRIVATE)
}