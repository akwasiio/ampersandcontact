package com.example.oppong.ampersandcontact.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class User(
    @SerializedName("_id") var id: String = "",
    @SerializedName("firstName") var firstName: String = "",
    @SerializedName("lastName") var lastName: String = "",
    @SerializedName("email") val email: String,
    @SerializedName("password") var password: String = "",
    @SerializedName("phoneNumber")var phoneNumber: String = "",
    @SerializedName("role") var role: String = "",
    @SerializedName("twitter") var twitter: String = "",
    @SerializedName("linkedIn") var linkedIn: String = "",
    @SerializedName("photo") var photo: String = ""
) : Serializable