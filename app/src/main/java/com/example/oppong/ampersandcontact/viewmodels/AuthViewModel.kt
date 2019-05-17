package com.example.oppong.ampersandcontact.viewmodels

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.ViewModel
import com.example.oppong.ampersandcontact.model.User
import com.example.oppong.ampersandcontact.repository.UserRepository
import java.io.File
import android.util.Base64
import android.util.Log
import java.io.ByteArrayOutputStream

class AuthViewModel : ViewModel() {
    private val userRepo = UserRepository()
    private var user: User? = null

    fun registerUser(
        mFirstName: String,
        mLastName: String,
        mPhone: String,
        mPassword: String,
        mEmail: String,
        mRole: String = "",
        mTwitter: String = "",
        mLinkedIn: String = "",
        mPhoto: String = ""
    ): Pair<String, User?> {
        user = User(firstName = mFirstName, lastName = mLastName, email = mEmail,linkedIn = mLinkedIn, twitter = mTwitter, role = mRole,
            phoneNumber = mPhone,password = mPassword , photo = mPhoto)
        Log.e("UserEntity: ", user.toString())
        return userRepo.registerUser(user!!)
    }

}