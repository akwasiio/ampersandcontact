package com.example.oppong.ampersandcontact.views

import android.annotation.TargetApi
import android.app.ProgressDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.oppong.ampersandcontact.R
import com.example.oppong.ampersandcontact.contracts.AuthenticationContract
import com.example.oppong.ampersandcontact.model.User
import com.example.oppong.ampersandcontact.presenters.SignInViewPresenter
import kotlinx.android.synthetic.main.activity_sign_in.*

class SignInActivity : AppCompatActivity(), AuthenticationContract.View {
    private lateinit var progressDialog: ProgressDialog
    private var presenter: SignInViewPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        progressDialog = ProgressDialog(this)
    }


    override fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        loginErrorText.visibility = View.VISIBLE
    }

    override fun showProgressDialog() {
        if (progressDialog.isShowing) {
            progressDialog.setMessage("Logging In...")
        } else {
            progressDialog.isIndeterminate = true
            progressDialog.setMessage("Logging In...")
            progressDialog.setCancelable(false)

            try {
                progressDialog.show()
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }

    override fun hideProgressDialog() {
        try {
            if (progressDialog.isShowing) {
                progressDialog.dismiss()
                progressDialog.hide()
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    override fun nextActivity(user: User) {
        val intent = Intent(applicationContext, HomeQRActivity::class.java)
        intent.putExtra("user", user)
        finishAffinity()
        finish()
        startActivity(intent)
    }

    fun loginButtonListener(view: View) {
        if (validateEmailAndPassword())
            presenter = SignInViewPresenter(
                this,
                loginEmailEditText.text.toString(),
                loginPasswordEditText.text.toString()
            )
    }

    private fun validateEmailAndPassword(): Boolean {
        var valid = true

        if (loginEmailEditText.text.isNullOrBlank() || !(Patterns.EMAIL_ADDRESS.matcher(
                loginEmailEditText.text
            ).matches())
        ) {
            loginEmailEditText.error = "Enter a valid email address"
            valid = false
        }

        if (loginPasswordEditText.text.isNullOrBlank()) {
            loginPasswordEditText.error = "This field cannot be blank."
            valid = false
        }


        return valid
    }

}
