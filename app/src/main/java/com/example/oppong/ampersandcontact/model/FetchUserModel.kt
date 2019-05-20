package com.example.oppong.ampersandcontact.model

import com.example.oppong.ampersandcontact.Utility
import com.example.oppong.ampersandcontact.contracts.ScanActivityContract
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FetchUserModel : ScanActivityContract.Model {
    override fun fetchUser(id: String, listener: ScanActivityContract.FetchProfileListener) {
        val webService = Utility.getWebService()
        val call = webService?.getUserProfile(id)

        call?.enqueue(object : Callback<UserResponse> {
            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                listener.onFailure(t)
            }

            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful)
                    listener.onSuccess(response)
                else
                    listener.onError(response)

            }
        })
    }

}