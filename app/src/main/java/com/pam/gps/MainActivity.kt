package com.pam.gps

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.firebase.auth.FirebaseAuth
import com.pam.gps.ui.current_trip.CurrentTripFragment
import com.pam.gps.ui.home.BottomNavigationDrawerFragment
import com.pam.gps.ui.home.HomeFragment
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber

class MainActivity : AppCompatActivity() {

  private val mRequiredPermissions = arrayOf(
    android.Manifest.permission.ACCESS_COARSE_LOCATION,
    android.Manifest.permission.ACCESS_FINE_LOCATION,
    android.Manifest.permission.ACCESS_BACKGROUND_LOCATION,
    android.Manifest.permission.FOREGROUND_SERVICE,
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

    navController.addOnDestinationChangedListener { _, destination, _ ->
      when (destination.id) {
        R.id.navigation_home -> {
          showBottomAppBar()
          bottom_appbar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_END
        }
        R.id.navigation_current_trip -> {
          showBottomAppBar()
          bottom_appbar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_CENTER
        }
        R.id.navigation_login -> {
          hideBottomAppBar()
        }
        else -> hideBottomAppBar()
      }
    }

    setSupportActionBar(bottom_appbar)

    createNotificationChannel()
    requestPermissions()
    setupFab()
  }

  override fun onOptionsItemSelected(item: MenuItem?): Boolean {
    when (item!!.itemId) {
      android.R.id.home -> {
        val bottomNavDrawerFragment = BottomNavigationDrawerFragment()
        bottomNavDrawerFragment.show(supportFragmentManager, bottomNavDrawerFragment.tag)
      }
    }
    return true
  }

  fun hideBottomAppBar() {
    bottom_appbar.performHide()
    bottom_appbar.visibility = View.GONE
    fab.visibility = View.GONE
  }

  private fun showBottomAppBar() {
    bottom_appbar.visibility = View.VISIBLE
    bottom_appbar.performShow()
    fab.visibility = View.VISIBLE
  }

  fun setupFab() {
    val currentDest = findNavController(R.id.nav_host_fragment).currentDestination?.label.toString()
    val isRunning = TrackerService.isRunning
    fab.setImageResource(getFabIcon(isRunning.value, currentDest))

    isRunning.subscribe { value ->
      val currentDest = findNavController(R.id.nav_host_fragment).currentDestination?.label.toString()
      fab.setImageResource(getFabIcon(value, currentDest))
    }

    fab.setOnClickListener {
      val currentDest = findNavController(R.id.nav_host_fragment).currentDestination?.label
      if (isRunning.value) {
        if (currentDest == "Home")
          findNavController(R.id.nav_host_fragment).navigate(R.id.action_navigation_home_to_navigation_current_trip)
        else {
          TrackerService.stop(this)
          findNavController(R.id.nav_host_fragment).navigate(R.id.action_navigation_trip_to_finishTripFragment)
        }
      }
      else {
        TrackerService.start(this)
        findNavController(R.id.nav_host_fragment).navigate(R.id.action_global_navigation_current_trip)
      }
    }
  }

  private fun getFabIcon(isServiceRunning: Boolean, currentDes: String?) : Int {
    return if (isServiceRunning) {
      when (currentDes) {
        resources.getString(R.string.title_home) -> R.drawable.ic_trip_pace
        resources.getString(R.string.title_trip) -> R.drawable.ic_stop_tracker
        else -> R.drawable.ic_stop_tracker
      }
    }
    else R.drawable.ic_start_tracker
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
        if (grantResults.isEmpty()) {
          //TODO[AR]: Think what to put here
          Timber.d("No permission grant response!")
          return
        }
        grantResults.forEachIndexed { i, res ->
          if (res != PackageManager.PERMISSION_GRANTED) {
            Timber.d("Permission for ${permissions[i]} was not granted")
            return
          }
        }
      }
    }
  }

  private fun isPermissionGranted(permission: String): Boolean {
    return ActivityCompat
      .checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
  }
}
