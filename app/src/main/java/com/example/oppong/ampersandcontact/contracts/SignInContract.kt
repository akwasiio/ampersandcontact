package com.example.oppong.ampersandcontact.contracts

import com.example.oppong.ampersandcontact.model.User
import com.example.oppong.ampersandcontact.model.UserAuthResponse
import retrofit2.Response

interface SignInContract {
    interface Model{
        fun loginUser(user: User, listener: LoginApiListener)
    }

    interface View{
        fun showMessage(message: String)

        fun showProgressDialog()
        fun hideProgressDialog()
        fun nextActivity(user: User)
    }

    interface Presenter{
        fun loginUser(user: User)
    }

    interface LoginApiListener{
        fun onSuccess(response: Response<UserAuthResponse>)
        fun onError(response: Response<UserAuthResponse>)
        fun onFailure(t: Throwable)
    }
}