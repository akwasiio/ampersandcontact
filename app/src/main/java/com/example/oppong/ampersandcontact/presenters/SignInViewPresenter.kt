package com.example.oppong.ampersandcontact.presenters

import android.util.Log
import com.example.oppong.ampersandcontact.contracts.SignInContract
import com.example.oppong.ampersandcontact.model.User
import com.example.oppong.ampersandcontact.model.UserAuthResponse
import com.example.oppong.ampersandcontact.model.UserModel
import retrofit2.Response

class SignInViewPresenter(view: SignInContract.View, email: String, password: String) : SignInContract.Presenter, SignInContract.LoginApiListener{
    private val user = User(email = email, password = password)

    override fun loginUser(user: User) {
        mView.showProgressDialog()
        userModel.loginUser(user, this)

    }

    override fun onSuccess(response: Response<UserAuthResponse>) {
        Log.d("mvpSuccess:::", response.body()?.user.toString())
        mView.hideProgressDialog()
        mView.nextActivity(response.body()!!.user)
    }

    override fun onError(response: Response<UserAuthResponse>) {
        mView.hideProgressDialog()
        mView.showMessage("Error occurred")
    }

    override fun onFailure(t: Throwable) {
        mView.hideProgressDialog()
        mView.showMessage(t.message!!)
    }

    private val userModel: SignInContract.Model = UserModel()
    private val mView = view
    init {
        loginUser(user)
    }
}