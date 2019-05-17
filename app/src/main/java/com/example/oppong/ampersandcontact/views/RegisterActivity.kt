package com.example.oppong.ampersandcontact

import androidx.appcompat.app.AppCompatActivity
import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.media.MediaScannerConnection
import android.net.Uri

import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.example.oppong.ampersandcontact.viewmodels.AuthViewModel
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.android.synthetic.main.activity_register.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class RegisterActivity : AppCompatActivity() {


    private val viewModel: AuthViewModel by lazy {
        ViewModelProviders.of(this).get(AuthViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
    }

    val CAMERA = 1
    val GALLERY = 0

    companion object {
        val IMAGE_DIRECTORY = "/ampersand"
    }


    fun onRegisterButtonClick(view: View) {
        if (areInputsValid()) {
            val fullNameSplit = fullNameEditText.text!!.split(" ")
            val firstName = fullNameSplit[0]
            val lastName = fullNameSplit[1]

            viewModel.registerUser(
                firstName = firstName,
                lastName = lastName,
                email = emailEditText.text.toString(),
                phone = phoneEditText.text.toString(),
                role = roleEditText.text.toString(),
                twitter = twitterEditText.text.toString(),
                linkedIn = linkedInEditText.text.toString(),
                photo = currentPhotoPath
            )
        } else Toast.makeText(this, "Invalid inputs", Toast.LENGTH_LONG).show()
    }

    private fun areInputsValid(): Boolean {
        var valid = true
        if (emailEditText.text.isNullOrBlank() || !Patterns.EMAIL_ADDRESS.matcher(emailEditText.text).matches()) {
            valid = false
            emailEditText.error = "Enter valid email address"
        }

        if (fullNameEditText.text.isNullOrBlank()) {
            valid = false
            fullNameEditText.error = "Full Name cannot be blank"
        }

        if (passwordEditText.text.isNullOrBlank() || passwordEditText.text!!.length < 8) {
            valid = false
            passwordEditText.error = "Password must be 8 characters or more"
        }

        if (phoneEditText.text.isNullOrBlank() || !Patterns.PHONE.matcher(phoneEditText.text).matches()) {
            valid = false
            phoneEditText.error = "Enter a valid phone number"
        }

        if (!twitterEditText.text.isNullOrBlank() && !twitterEditText.text!!.startsWith("@")) {
            valid = false
            twitterEditText.error = "Twitter handle must begin with @"
        }

        return valid
    }

    fun showChooserDialog(view: View) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            requestMultiplePerms()
        }
        val chooserDialog = AlertDialog.Builder(this)
        chooserDialog.setTitle("Select Action")
        val chooserDialogItems = arrayOf("Select a photo from gallery", "Capture photo from camera")
        chooserDialog.setItems(chooserDialogItems) { _, which ->
            when (which) {
                0 -> chooseImageFromGallery()
                1 -> takeImageFromCamera()
            }
        }
        chooserDialog.show()
    }

    private fun chooseImageFromGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, GALLERY)
    }

    lateinit var photoURI: Uri
    private fun takeImageFromCamera() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    //TODO: HANDLE ERROR HERE
                    null
                }
                photoFile?.also {
                    photoURI = FileProvider.getUriForFile(
                        this,
                        "com.example.android.fileprovider",
                        it
                    )
                }
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                startActivityForResult(takePictureIntent, CAMERA)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GALLERY) {
                if (data != null) {
                    val contentUri = data.data
                    try {
                        val bitmap =
                            MediaStore.Images.Media.getBitmap(this.contentResolver, contentUri)
                        saveImage(bitmap)
                        profileImageView.setImageBitmap(bitmap)
                        makeProfileImageVisible()

                    } catch (e: IOException) {
//                        Snackbar.make(
//                            registerpage,
//                            "An error occurred when using camera.",
//                            Snackbar.LENGTH_LONG
//                        )

                    }
                }
            } else if (requestCode == CAMERA) {
//                val thumbnail = data!!.extras!!.get("data") as Bitmap
                setPic()
//                saveImage(thumbnail)
            }
        }
    }

    fun setPic() {
//        val targetWidth = profileImageView.width
//        val targetHeight = profileImageView.height
//
//        val bmOptions = BitmapFactory.Options()
//        bmOptions.inJustDecodeBounds = true
//        BitmapFactory.decodeFile(currentPhotoPath, bmOptions)
//        val photoWidth = bmOptions.outWidth
//        val photoHeight = bmOptions.outHeight
//
//        // Determine how much to scale down the image.
//        val scaleFactor = Math.min(photoWidth / targetWidth, photoHeight / targetHeight)
//
//        // decode the image file into a Bitmap sized to fit the view
//        bmOptions.inJustDecodeBounds = false
//        bmOptions.inSampleSize = scaleFactor
//        bmOptions.inPurgeable = true

//        val bitmap = BitmapFactory.decodeFile(currentPhotoPath, bmOptions)
        val bitmap = BitmapFactory.decodeFile(currentPhotoPath)
        val perfectBitmap = checkIfRotationIsRequired(bitmap)
        profileImageView.setImageBitmap(perfectBitmap)
        makeProfileImageVisible()
    }

    private fun makeProfileImageVisible() {
        profilePictureContainer.visibility = View.VISIBLE
        addProfilePictureBtn.visibility = View.GONE
        profilePhotoLabel.visibility = View.GONE
    }

    private fun saveImage(mBitmap: Bitmap): String {
        val bytes = ByteArrayOutputStream()
        mBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes)
        val profilePictureDir =
            File(Environment.getExternalStorageDirectory().toString() + IMAGE_DIRECTORY)

        if (!profilePictureDir.exists()) {
            profilePictureDir.mkdirs()
        }

        try {
            val f =
                File(profilePictureDir, ((Calendar.getInstance().timeInMillis.toString() + ".jpg")))
            f.createNewFile()
            val fo = FileOutputStream(f)
            fo.write(bytes.toByteArray())

            MediaScannerConnection.scanFile(this, arrayOf(f.path), arrayOf("image/jpeg"), null)
            fo.close()

            return f.absolutePath
        } catch (ex: IOException) {
            Toast.makeText(this, "Error occurred while saving image", Toast.LENGTH_LONG).show()
        }

        return ""
    }


    private var currentPhotoPath: String = ""

    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        ).apply {
            currentPhotoPath = absolutePath
        }

    }

    fun checkIfRotationIsRequired(bitmap: Bitmap): Bitmap {
        val exifInterface = ExifInterface(currentPhotoPath)
        val orientation = exifInterface.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_UNDEFINED
        )

        return when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(bitmap, 90f)
            ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(bitmap, 180f)
            ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(bitmap, 270f)
            else -> bitmap
        }
    }

    fun rotateImage(source: Bitmap, angle: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(angle)
        return Bitmap.createBitmap(source, 0, 0, source.width, source.height, matrix, true)
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    private fun requestMultiplePerms() {
        Dexter.withActivity(this)
            .withPermissions(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ).withListener(
                object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                        if (report.isAnyPermissionPermanentlyDenied) {
                            //open device settings.
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        permissions: MutableList<PermissionRequest>?,
                        token: PermissionToken?
                    ) {
                        token?.continuePermissionRequest()
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
