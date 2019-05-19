package com.example.oppong.ampersandcontact.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.oppong.ampersandcontact.R
import com.example.oppong.ampersandcontact.model.User
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val receivedIntent = intent
        val user = receivedIntent.getSerializableExtra("user") as User

        profileFullNameTextView.text = user.firstName.plus(user.lastName)
        profileRoleTextView.text = user.role

        Picasso.with(this).load(user.photo).placeholder(R.drawable.ic_user_tb).fit().centerCrop()
            .into(profileImageCircleView)

        profileMail.text = user.email
        profileTelephone.text = user.phoneNumber

    }


}
