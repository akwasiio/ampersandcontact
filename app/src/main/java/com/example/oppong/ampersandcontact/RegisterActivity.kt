package com.example.oppong.ampersandcontact

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.telephony.PhoneNumberUtils
import android.util.Patterns
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_register.*


class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
    }

    fun onRegisterButtonClick(view: View){
        if(areInputsValid()) Toast.makeText(this, "Inputs are valid.", Toast.LENGTH_LONG).show()
        else Toast.makeText(this, "Invalid inputs", Toast.LENGTH_LONG).show()
    }

    fun areInputsValid(): Boolean {
        var valid = true
        if(emailEditText.text.isNullOrBlank() || !Patterns.EMAIL_ADDRESS.matcher(emailEditText.text).matches()){
            valid = false
            emailEditText.error = "Enter valid email address"
        }

        if(fullNameEditText.text.isNullOrBlank()){
            valid = false
            fullNameEditText.error = "Full Name cannot be blank"
        }

        if(passwordEditText.text.isNullOrBlank() || passwordEditText.text!!.length < 8){
            valid = false
            passwordEditText.error = "Password must be 8 characters or more"
        }

        if(phoneEditText.text.isNullOrBlank() ||!Patterns.PHONE.matcher(phoneEditText.text).matches()){
            valid = false
            phoneEditText.error = "Enter a valid phone number"
        }

        if(!twitterEditText.text.isNullOrBlank() && !twitterEditText.text!!.startsWith("@")){
            valid = false
            twitterEditText.error = "Twitter handle must begin with @"
        }

        return valid
    }
}
