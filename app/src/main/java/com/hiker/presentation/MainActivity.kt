package com.hiker.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.activity_main.*
import android.content.pm.PackageManager
import android.util.Base64
import android.util.Log
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.hiker.R.layout.activity_main)

        navController = Navigation.findNavController(this, com.hiker.R.id.nav_host_fragment)
        bottomNavigation.setupWithNavController(navController)
        navController.addOnDestinationChangedListener{ _, destination, _ ->
            when (destination.id) {
                com.hiker.R.id.loginView -> bottomNavigation.visibility = View.INVISIBLE
            }

        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, null)
    }
}
