package com.example.oppong.ampersandcontact.model

import com.google.gson.annotations.SerializedName

class UserResponse (@SerializedName("user")val user: User, @SerializedName("token") val token: String)
