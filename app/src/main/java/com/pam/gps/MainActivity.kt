package com.pam.gps

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

  private val mRequiredPermissions = arrayOf(
    android.Manifest.permission.ACCESS_FINE_LOCATION,
    android.Manifest.permission.READ_EXTERNAL_STORAGE,
    android.Manifest.permission.ACCESS_MEDIA_LOCATION
  )

  private val cRequestPermissionsCode = 4321

  private lateinit var mAppBarConfiguration: AppBarConfiguration

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    //setSupportActionBar(bottom_appbar)

    val navController = findNavController(R.id.nav_host_fragment)

    if (FirebaseAuth.getInstance().currentUser == null)
      navController.navigate(R.id.action_navigation_home_to_navigation_login)

    if (intent.extras?.get("SENDER") == "Notification") {
      navController.navigate(R.id.navigation_current_trip)
    }

    createNotificationChannel()
    requestPermissions()
  }

  private fun createNotificationChannel() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      val serviceChannel = NotificationChannel(
        getString(R.string.notification_channel_id),
        "Tracker Service Channel",
        NotificationManager.IMPORTANCE_LOW
      )

      getSystemService(NotificationManager::class.java)!!
        .createNotificationChannel(serviceChannel)
    }
  }

  private fun requestPermissions() {
    ActivityCompat.requestPermissions(this, mRequiredPermissions, cRequestPermissionsCode)
  }

  override fun onRequestPermissionsResult(
    requestCode: Int,
    permissions: Array<String>,
    grantResults: IntArray
  ) {
    when (requestCode) {
      cRequestPermissionsCode -> {
        if ((grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED))
        //TODO[AR]: Fix this shit, show a screen with info
          requestPermissions()
        return
      }
      else -> {
      }
    }
  }

  private fun isPermissionGranted(permission: String): Boolean {
    return ActivityCompat
      .checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
  }
}
