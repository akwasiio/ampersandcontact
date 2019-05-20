package com.example.oppong.ampersandcontact

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.util.Log
import com.example.oppong.ampersandcontact.model.User
import com.example.oppong.ampersandcontact.rest.ApiClient
import com.example.oppong.ampersandcontact.rest.WebService

class Utility {
    companion object {
        fun getWebService(): WebService? {
            return ApiClient.getClient()?.create(WebService::class.java)
        }

        fun addSharedPrefs(user: User, context: Context){
            val prefs = context.getSharedPreferences("user_details", MODE_PRIVATE)
            val editor = prefs.edit()
            editor.putString("firstName", user.firstName)
            editor.putString("lastName", user.lastName)
            editor.putString("user_id", user.id)
            editor.putString("phone", user.phoneNumber)
            editor.putString("email", user.email)
            editor.putString("role", user.role)
            editor.putString("twitter", user.twitter)
            editor.putString("linkedIn", user.linkedIn)
            editor.putString("photo", user.photo)
            editor.putBoolean("isLoggedIn", true)
            editor.apply()
        }

        fun makeNameTitleCase(name: String): String{
            val separated = name.split(" ")
            var newString = ""
            for (word in separated) {
                newString += word.replaceFirst(word[0], word[0].toUpperCase()).plus(" ")
            }
            return newString
        }

    }
}