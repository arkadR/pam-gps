package com.pam.gps

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber

class MainActivity : AppCompatActivity() {

  private val mRequiredPermissions = arrayOf(
    android.Manifest.permission.ACCESS_FINE_LOCATION,
    android.Manifest.permission.READ_EXTERNAL_STORAGE,
    android.Manifest.permission.ACCESS_MEDIA_LOCATION)

  private val cRequestPermissionsCode = 4321

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    val navView: BottomNavigationView = findViewById(R.id.nav_view)

    val navController = findNavController(R.id.nav_host_fragment)
    // Passing each menu ID as a set of Ids because each
    // menu should be considered as top level destinations.
    val appBarConfiguration = AppBarConfiguration(
      setOf(
        R.id.navigation_trip, R.id.navigation_home, R.id.navigation_map, R.id.navigation_login
      )
    )
    setupActionBarWithNavController(navController, appBarConfiguration)
    navView.setupWithNavController(navController)
    navController.addOnDestinationChangedListener { _, destination, _ ->
      when (destination.id) {
        R.id.navigation_login -> hideBottomNavigation()
        else -> showBottomNavigation()
      }
    }

    if(FirebaseAuth.getInstance().currentUser == null)
        navController.navigate(R.id.action_navigation_home_to_navigation_login)

    if (intent.extras?.get("SENDER") == "Notification") {
      navController.navigate(R.id.navigation_trip)
    }

    createNotificationChannel()
    requestPermissions()
  }


  private fun hideBottomNavigation() {
    nav_view.run {
      if (visibility == View.VISIBLE && alpha == 1f) {
        animate()
          .alpha(0f)
          .withEndAction { visibility = View.GONE }
          .duration = 250
      }
    }
  }

  private fun showBottomNavigation() {
    nav_view.run {
      if (visibility == View.GONE && alpha == 0f) {
        animate()
          .alpha(1f)
          .withEndAction { visibility = View.VISIBLE }
          .duration = 250
      }
    }
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

  override fun onRequestPermissionsResult(requestCode: Int,
                                          permissions: Array<String>,
                                          grantResults: IntArray) {
    when (requestCode) {
      cRequestPermissionsCode -> {
        if ((grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED))
          //TODO[AR]: Fix this shit, show a screen with info
          requestPermissions()
        return
      }
      else -> {}
    }
  }

  private fun isPermissionGranted(permission: String): Boolean {
    return ActivityCompat
      .checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
  }
}
