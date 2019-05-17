package com.example.oppong.ampersandcontact.views

import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.oppong.ampersandcontact.R
import com.example.oppong.ampersandcontact.model.User
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_home_qr.*

class HomeQRActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_qr)

        val receivedIntent = intent
        val user = receivedIntent.getSerializableExtra("user") as User
        val barcodeBitmap = generateQR(user.id)
        barcodeView.setImageBitmap(barcodeBitmap)

        userFullNameTextView.text = user.firstName.plus(" ".plus(user.lastName))
        userRoleTextView.text = user.role

        Picasso.with(this).load(user.photo).fit().centerCrop().into(circle_profile_image)
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
}
