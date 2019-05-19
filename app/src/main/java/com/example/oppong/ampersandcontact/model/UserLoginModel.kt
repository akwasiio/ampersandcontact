package com.example.oppong.ampersandcontact.model

import android.util.Log
import com.example.oppong.ampersandcontact.rest.ApiClient
import com.example.oppong.ampersandcontact.rest.WebService
import com.example.oppong.ampersandcontact.contracts.AuthenticationContract
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserLoginModel : AuthenticationContract.Model {
    override fun authenticateUser(user: User, listener: AuthenticationContract.AuthenticationApiListener) {
        val webService = ApiClient.getClient()?.create(WebService::class.java)
        val call = webService?.login(user)

        call?.enqueue(object : Callback<UserAuthResponse>{
            override fun onFailure(call: Call<UserAuthResponse>, t: Throwable) {
                listener.onFailure(t)
            }

            override fun onResponse(
                call: Call<UserAuthResponse>,
                response: Response<UserAuthResponse>
            ) {
                if(response.isSuccessful){
                    listener.onSuccess(response)
                    Log.e("Success::", response.body()?.user.toString())
                }

                else{
                    listener.onError(response)
                }

            }

        })
    }


}