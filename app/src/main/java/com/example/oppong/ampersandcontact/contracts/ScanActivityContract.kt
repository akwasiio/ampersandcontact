package com.example.oppong.ampersandcontact.contracts

import com.example.oppong.ampersandcontact.model.User
import com.example.oppong.ampersandcontact.model.UserResponse
import retrofit2.Response

interface ScanActivityContract {
    interface Model{
        fun fetchUser(id: String, listener: FetchProfileListener)
    }

    interface View {
        fun showProgressDialog()
        fun hideProgressDialog()
        fun showMessage(message: String)
        fun openMemberProfile(user: User)

    }

    interface Presenter {
        fun fetchUser(id: String)
    }

    interface FetchProfileListener {
        fun onSuccess(response: Response<UserResponse>)
        fun onError(response: Response<UserResponse>)
        fun onFailure(t: Throwable)
    }

}