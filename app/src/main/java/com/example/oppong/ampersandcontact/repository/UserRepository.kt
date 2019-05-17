package com.example.oppong.ampersandcontact.repository

import android.util.Log
import com.example.oppong.ampersandcontact.model.User
import com.example.oppong.ampersandcontact.model.UserAuthResponse
import com.example.oppong.ampersandcontact.rest.ApiClient
import com.example.oppong.ampersandcontact.rest.WebService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRepository {
    private val webService: WebService? = ApiClient.getClient()?.create(WebService::class.java)
    private var call: Call<UserAuthResponse>? = null

    private lateinit var messageAndObject: Pair<String, User?>

    fun registerUser(user: User): Pair<String, User?> {
        call = webService?.register(user)

        call!!.enqueue(object : Callback<UserAuthResponse> {
            override fun onFailure(call: Call<UserAuthResponse>, t: Throwable) {
                Log.e("NETWORK", "Network error. Check connection")
                Log.e("FailureMessage:", t.message)
                messageAndObject = Pair("network error", null)
            }

            override fun onResponse(
                call: Call<UserAuthResponse>,
                response: Response<UserAuthResponse>
            ) {
                if(response.body() != null){
                    messageAndObject = Pair("registration successful", response.body()!!.user)
                }
            }

        })
        return messageAndObject
    }

//    fun login(user: User) {
//        call = webService?.login(user)
//
//        call?.enqueue(object : Callback<UserAuthResponse> {
//            override fun onFailure(call: Call<UserAuthResponse>, t: Throwable) {
//                Log.e("ConnectionFailed", t.message)
//            }
//
//            override fun onResponse(call: Call<UserAuthResponse>, response: Response<UserAuthResponse>) {
//                Log.e("Response:", response.code().toString())
//            }
//
//        })
//    }
}