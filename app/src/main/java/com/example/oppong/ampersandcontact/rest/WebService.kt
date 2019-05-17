package com.example.oppong.ampersandcontact.rest

import com.example.oppong.ampersandcontact.model.User
import com.example.oppong.ampersandcontact.model.UserAuthResponse
import retrofit2.Call
import retrofit2.http.*

interface WebService {
    @Headers("Content-Type: application/json")
    @POST("register")
    fun register(@Body user: User): Call<UserAuthResponse>

    @Headers("Content-Type: application/json")
    @POST("login")
    fun login(@Body user: User): Call<UserAuthResponse>

    @Headers(
        value = ["Content-type: application/json",
            "x-access-token: eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJfaWQiOiI1Yzg2MjNhMzdiNDU3OTAwMD" +
                    "RhMTZhNjUiLCJpYXQiOjE1NTIyOTQ4ODd9.eMXlcE4e_5N2fSxrQaeYJyGCzBnhL_BeenaroWsaZ9s"]
    )
    @GET("profile/{id}")
    fun getUserProfile(@Path("id") id: String)
}