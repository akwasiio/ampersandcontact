package com.example.oppong.ampersandcontact.views

import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.oppong.ampersandcontact.R
import com.example.oppong.ampersandcontact.Utility
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val receivedIntent = intent


        val prefs = getSharedPreferences("user_details", Context.MODE_PRIVATE)
        val firstName = prefs.getString("firstName", " ")
        val lastName = prefs.getString("lastName", " ")
        val photo = prefs.getString("photo", " ")
        val email = prefs.getString("email", " ")
        val phoneNumber = prefs.getString("phone", " ")
        val role = prefs.getString("role", " ")

        val extraString = receivedIntent.getStringExtra("newHeaderText")
        if(extraString != null)
            profileHeaderText.text = extraString

        profileFullNameTextView.text = Utility.makeNameTitleCase(firstName.plus(" ".plus(lastName)))
        profileRoleTextView.text = role

        Picasso.with(this).load(photo).placeholder(R.drawable.ic_user).fit().centerCrop()
            .into(profileImageCircleView)

        profileMail.text = email
        profileTelephone.text = phoneNumber

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
        finish()
        startActivity(Intent(this, HomeQRActivity::class.java))
    }

    fun onBackArrowPressed(view: View) {
        onBackPressed()
    }

}
