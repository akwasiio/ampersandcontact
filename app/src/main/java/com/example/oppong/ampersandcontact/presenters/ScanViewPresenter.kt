package com.example.oppong.ampersandcontact.presenters

import com.example.oppong.ampersandcontact.contracts.ScanActivityContract
import com.example.oppong.ampersandcontact.model.FetchUserModel
import com.example.oppong.ampersandcontact.model.UserResponse
import retrofit2.Response

class ScanViewPresenter(view: ScanActivityContract.View, id: String) : ScanActivityContract.Presenter,
    ScanActivityContract.FetchProfileListener {
    val mView = view
    private val fetchUserModel: ScanActivityContract.Model = FetchUserModel()


    override fun fetchUser(id: String) {
        mView.showProgressDialog()
        fetchUserModel.fetchUser(id, this)
    }

    override fun onSuccess(response: Response<UserResponse>) {
        mView.hideProgressDialog()
        mView.openMemberProfile(response.body()!!.user)
    }

    override fun onError(response: Response<UserResponse>) {
        mView.hideProgressDialog()
        mView.showMessage(response.message())
    }

    override fun onFailure(t: Throwable) {
        mView.hideProgressDialog()
        mView.showMessage(t.message!!)

    }

    init {
        fetchUser(id)
    }


}