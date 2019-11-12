package com.hiker.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.hiker.R

class SplashScreenActivity : AppCompatActivity() {

    private val splashTime = 2000L
    private lateinit var myHandler : Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        myHandler = Handler()

        myHandler.postDelayed({
            goToMainActivity()
        }, splashTime)
    }

    private fun goToMainActivity(){
        val mainActivityIntent = Intent(applicationContext, MainActivity::class.java)
        startActivity(mainActivityIntent)
        finish()
    }
}
