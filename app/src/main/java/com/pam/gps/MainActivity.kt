package com.pam.gps

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


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

}
