package com.example.projectfinder

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.projectfinder.util.MySharePreferences
import com.google.android.material.bottomnavigation.BottomNavigationView

class AppActivity : AppCompatActivity()
{
    private lateinit var mySharePreferences: MySharePreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app)
        supportActionBar?.hide()

        mySharePreferences = MySharePreferences(this)

        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        val appBarConfiguration = AppBarConfiguration(setOf(
            R.id.navigation_notifications, R.id.navigation_specialists, R.id.navigation_profile, R.id.navigation_projects))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }
}