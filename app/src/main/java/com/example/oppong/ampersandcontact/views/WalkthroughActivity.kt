package com.example.oppong.ampersandcontact

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class WalkthroughActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_walkthrough)
    }

    fun registerButtonClick(view: View){
        startActivity(Intent(this, RegisterActivity::class.java))
    }

    fun onSignInButtonClick(view: View){
        startActivity(Intent(this, SignInActivity::class.java))
    }
}
