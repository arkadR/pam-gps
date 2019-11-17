package com.pam.gps

import android.content.pm.PackageManager
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

class MainActivity : AppCompatActivity() {

  private val mLocationPermissions = arrayOf(
    android.Manifest.permission.ACCESS_FINE_LOCATION,
    android.Manifest.permission.ACCESS_COARSE_LOCATION)

  private val CODE_LOCATION_PERMISSIONS = 1234

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

  private fun requestPermissions() {
    if (ActivityCompat
        .checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
      ActivityCompat.requestPermissions(this, mLocationPermissions, CODE_LOCATION_PERMISSIONS)
    } else {
      // Show rationale and request permission.
    }
  }

  override fun onRequestPermissionsResult(requestCode: Int,
                                          permissions: Array<String>, grantResults: IntArray) {
    when (requestCode) {
      CODE_LOCATION_PERMISSIONS -> {
        if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {

        } else {
          requestPermissions()
        }
        return
      }
      else -> {}
    }
  }

}
