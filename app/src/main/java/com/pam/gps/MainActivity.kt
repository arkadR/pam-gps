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
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

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

    val navController = findNavController(R.id.nav_host_fragment)

    if (FirebaseAuth.getInstance().currentUser == null)
      navController.navigate(R.id.action_navigation_home_to_navigation_login)

    if (intent.extras?.get("SENDER") == "Notification") {
      navController.navigate(R.id.navigation_current_trip)
    }

    findNavController(R.id.nav_host_fragment).addOnDestinationChangedListener { _, destination, _ ->
      when (destination.id) {
        R.id.navigation_home -> {
          showBottomAppBar()
          bottom_appbar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_END
        }

        R.id.navigation_current_trip -> {
          showBottomAppBar()
          bottom_appbar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_CENTER
        }
        else -> hideBottomAppBar()
      }
    }

    createNotificationChannel()
    requestPermissions()
  }

  private fun hideBottomAppBar() {
    bottom_appbar.performHide()
    fab.visibility = View.GONE
  }

  private fun showBottomAppBar() {
    bottom_appbar.performShow()
    fab.visibility = View.VISIBLE
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
