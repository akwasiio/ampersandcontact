package com.example.oppong.ampersandcontact.contracts

import com.example.oppong.ampersandcontact.model.User
import com.example.oppong.ampersandcontact.model.UserResponse
import retrofit2.Response

interface AuthenticationContract {
    interface Model{
        fun authenticateUser(user: User, listener: AuthenticationApiListener)
    }

    interface View{
        fun showMessage(message: String)

        fun showProgressDialog()
        fun hideProgressDialog()
        fun nextActivity(user: User)
    }

    interface Presenter{
        fun authenticateUser(user: User)
    }

    interface AuthenticationApiListener{
        fun onSuccess(response: Response<UserResponse>)
        fun onError(response: Response<UserResponse>)
        fun onFailure(t: Throwable)
    }
}