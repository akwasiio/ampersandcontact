package com.example.oppong.ampersandcontact.views

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.oppong.ampersandcontact.R

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val handler = Handler()
        handler.postDelayed({
            checkIfUserIsLoggedIn()
        }, 5000)

    }

    private fun checkIfUserIsLoggedIn() {
        val prefs = getSharedPreferences("user_details", Context.MODE_PRIVATE)
        if (!prefs.getBoolean("isLoggedIn", false)) {
            finish()
            startActivity(Intent(this, WalkthroughActivity::class.java))
        } else {
            finish()
            startActivity(Intent(this, HomeQRActivity::class.java))
        }
    }
}
