package com.example.oppong.ampersandcontact.presenters

import android.util.Log
import com.example.oppong.ampersandcontact.contracts.AuthenticationContract
import com.example.oppong.ampersandcontact.model.User
import com.example.oppong.ampersandcontact.model.UserAuthResponse
import com.example.oppong.ampersandcontact.model.UserLoginModel
import retrofit2.Response

class SignInViewPresenter(view: AuthenticationContract.View, email: String, password: String) : AuthenticationContract.Presenter, AuthenticationContract.AuthenticationApiListener{
    private val user = User(email = email, password = password)

    override fun authenticateUser(user: User) {
        mView.showProgressDialog()
        userModel.authenticateUser(user, this)

    }

    override fun onSuccess(response: Response<UserAuthResponse>) {
        Log.d("mvpSuccess:::", response.body()?.user.toString())
        mView.hideProgressDialog()
        mView.nextActivity(response.body()!!.user)
    }

    override fun onError(response: Response<UserAuthResponse>) {
        mView.hideProgressDialog()
        mView.showMessage(response.message())
    }

    override fun onFailure(t: Throwable) {
        mView.hideProgressDialog()
        mView.showMessage("Network Error")
    }

    private val userModel: AuthenticationContract.Model = UserLoginModel()
    private val mView = view
    init {
        authenticateUser(user)
    }
}