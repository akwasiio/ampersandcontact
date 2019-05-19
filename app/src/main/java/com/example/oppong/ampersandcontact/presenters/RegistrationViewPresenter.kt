package com.example.oppong.ampersandcontact.presenters

import com.example.oppong.ampersandcontact.contracts.AuthenticationContract
import com.example.oppong.ampersandcontact.model.User
import com.example.oppong.ampersandcontact.model.UserAuthResponse
import com.example.oppong.ampersandcontact.model.UserLoginModel
import com.example.oppong.ampersandcontact.model.UserRegistrationModel
import retrofit2.Response

class RegistrationViewPresenter(
    mView: AuthenticationContract.View,
    mFirstName: String,
    mLastName: String,
    mPhone: String,
    mPassword: String,
    mEmail: String,
    mRole: String = "",
    mTwitter: String = "",
    mLinkedIn: String = "",
    mPhoto: String = ""
) : AuthenticationContract.Presenter, AuthenticationContract.AuthenticationApiListener {
    private val view = mView
    private val userModel: AuthenticationContract.Model = UserRegistrationModel()

    val user = User(firstName = mFirstName, lastName = mLastName, email = mEmail,linkedIn = mLinkedIn, twitter = mTwitter, role = mRole,
        phoneNumber = mPhone,password = mPassword , photo = mPhoto)

    init {
        authenticateUser(user)
    }

    override fun authenticateUser(user: User) {
        view.showProgressDialog()
        userModel.authenticateUser(user, this)
    }

    override fun onSuccess(response: Response<UserAuthResponse>) {
        view.hideProgressDialog()
        view.nextActivity(response.body()!!.user)
    }

    override fun onError(response: Response<UserAuthResponse>) {
        view.hideProgressDialog()
        view.showMessage("Error occurred. Try again.")
    }

    override fun onFailure(t: Throwable) {
        view.hideProgressDialog()
        view.showMessage(t.message!!)
    }

}