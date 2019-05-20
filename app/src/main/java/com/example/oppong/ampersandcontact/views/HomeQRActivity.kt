package com.example.oppong.ampersandcontact.views

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.oppong.ampersandcontact.R
import com.example.oppong.ampersandcontact.Utility
import com.example.oppong.ampersandcontact.model.User
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_home_qr.*

class HomeQRActivity : AppCompatActivity() {

    var user: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_qr)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
            requestCameraPermission()

        val prefs = getSharedPreferences("user_details", Context.MODE_PRIVATE)
        val firstName = prefs.getString("firstName", " ")
        val lastName = prefs.getString("lastName", " ")
        val photo = prefs.getString("photo", " ")
        val userId = prefs.getString("user_id", " ")
        val role = prefs.getString("role", " ")

        val barcodeBitmap = generateQR(userId)
        barcodeView.setImageBitmap(barcodeBitmap)

        userFullNameTextView.text = Utility.makeNameTitleCase(firstName.plus(" ".plus(lastName)))
        userRoleTextView.text = role

        Picasso.with(this).load(photo).fit().centerCrop().placeholder(R.drawable.ic_user_tb).into(circle_profile_image)
    }

    fun openProfilePage(view: View){
        val intent = Intent(this, ProfileActivity::class.java)
//        intent.putExtra("user", user)
        startActivity(intent)
    }

    private fun generateQR(content: String): Bitmap {
        val bitMatrix: BitMatrix =
            MultiFormatWriter().encode(
                content,
                BarcodeFormat.QR_CODE,
                800,
                800,
                null
            )
        val barcodeEncoder = BarcodeEncoder()
        return barcodeEncoder.createBitmap(bitMatrix)

    }

    fun scanQRListener(view: View){
        startActivity(Intent(this, ScanActivity::class.java))
    }

    private fun requestCameraPermission() {
        Dexter.withActivity(this).withPermission(android.Manifest.permission.CAMERA).withListener(
            object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse?) {

                }

                override fun onPermissionRationaleShouldBeShown(
                    permission: PermissionRequest?,
                    token: PermissionToken?
                ) {
                    token?.continuePermissionRequest()
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse) {
                    if (response.isPermanentlyDenied) {
                        // open device settings
                    }
                }
            }
        ).withErrorListener {
            Toast.makeText(
                this,
                "An error occurred when requesting permission. Try again.",
                Toast.LENGTH_LONG
            ).show()
        }.onSameThread().check()

    }

}
