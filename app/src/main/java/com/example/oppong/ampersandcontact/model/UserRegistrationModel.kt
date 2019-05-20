package com.example.oppong.ampersandcontact.model

import com.example.oppong.ampersandcontact.contracts.AuthenticationContract
import com.example.oppong.ampersandcontact.rest.ApiClient
import com.example.oppong.ampersandcontact.rest.WebService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRegistrationModel : AuthenticationContract.Model {
    override fun authenticateUser(
        user: User,
        listener: AuthenticationContract.AuthenticationApiListener
    ) {
         val webService = ApiClient.getClient()?.create(WebService::class.java)
        val call = webService?.register(user)

        call?.enqueue(object : Callback<UserResponse> {
            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                listener.onFailure(t)
            }

            override fun onResponse(
                call: Call<UserResponse>,
                response: Response<UserResponse>
            ) {
                if (response.isSuccessful)
                    listener.onSuccess(response)
                else
                    listener.onError(response)
            }

        })
    }
}