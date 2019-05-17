package com.example.oppong.ampersandcontact.model

import com.google.gson.annotations.SerializedName

class UserAuthResponse (@SerializedName("user")val user: User, @SerializedName("token") val token: String)
