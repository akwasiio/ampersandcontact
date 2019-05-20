package com.example.oppong.ampersandcontact.views

import android.Manifest
import android.annotation.TargetApi
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.ImageFormat
import android.hardware.camera2.*
import android.media.ImageReader
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

import android.util.Log
import android.view.SurfaceHolder
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.oppong.ampersandcontact.R
import com.example.oppong.ampersandcontact.contracts.ScanActivityContract
import com.example.oppong.ampersandcontact.model.User
import com.example.oppong.ampersandcontact.presenters.ScanViewPresenter
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.Frame
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import kotlinx.android.synthetic.main.activity_scan.*
import java.io.IOException

class ScanActivity : AppCompatActivity(), ScanActivityContract.View {
    private lateinit var progressDialog: ProgressDialog
    private var scanViewPresenter: ScanActivityContract.Presenter? = null

    override fun showProgressDialog() {
        if (progressDialog.isShowing) {
            progressDialog.setMessage("Fetching user. Please wait...")
        } else {
            progressDialog.isIndeterminate = true
            progressDialog.setMessage("Fetching user. Please wait...")
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

    override fun showMessage(message: String) {
        hideProgressDialog()
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun openMemberProfile(user: User) {
        val intent = Intent(this, ProfileActivity::class.java)
        intent.putExtra("user", user)
        intent.putExtra("newHeaderText", "Member Profile")
        finish()
        startActivity(intent)
    }

    private lateinit var barcodeDetector: BarcodeDetector
    private lateinit var cameraSource: CameraSource

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan)
        progressDialog = ProgressDialog(this)

        barcodeDetector = BarcodeDetector.Builder(this)
            .setBarcodeFormats(Barcode.QR_CODE)
            .build()

        cameraSource = CameraSource.Builder(this, barcodeDetector)
            .setRequestedPreviewSize(210, 200)
            .setAutoFocusEnabled(true)
            .build()


        scannerPreview.holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
//                startCameraPreview(width, height)
            }

            override fun surfaceDestroyed(holder: SurfaceHolder?) {
                cameraSource.stop()
            }

            @RequiresApi(Build.VERSION_CODES.M)
            override fun surfaceCreated(holder: SurfaceHolder?) {

                try {
                    checkSelfPermission(Manifest.permission.CAMERA)
                    cameraSource.start(scannerPreview.holder)
                } catch (ex: IOException) {
                    ex.printStackTrace()
                }
            }
        })

        barcodeDetector.setProcessor(object : Detector.Processor<Barcode> {
            override fun release() {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun receiveDetections(detections: Detector.Detections<Barcode>?) {
                val barcodes = detections?.detectedItems
                if (barcodes?.size() != 0) {
                    Log.d("Barcode Detected", barcodes?.valueAt(0)?.displayValue)
                    scanViewPresenter = ScanViewPresenter(this@ScanActivity, barcodes?.valueAt(0)?.rawValue!!)
                    cameraSource.stop()
                }
            }

        })
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun startCameraPreview(desiredWidth: Int, desiredHeight: Int) {
        try {
            // TODO
            val cameraBkgHandler = Handler()

            val cameraManager = applicationContext!!.getSystemService(Context.CAMERA_SERVICE) as CameraManager

            cameraManager.cameraIdList.find {
                val characteristics = cameraManager.getCameraCharacteristics(it)
                val cameraDirection = characteristics.get(CameraCharacteristics.LENS_FACING)

                return@find cameraDirection != null && cameraDirection == CameraCharacteristics.LENS_FACING_BACK
            }?.let {
                val cameraStateCallback = object : CameraDevice.StateCallback() {
                    override fun onOpened(camera: CameraDevice) {
                        val barcodeDetector = BarcodeDetector.Builder(applicationContext)
                            .setBarcodeFormats(Barcode.QR_CODE)
                            .build()

                        if (!barcodeDetector.isOperational) {
                            // TODO
                        }

                        val imgReader = ImageReader.newInstance(desiredWidth, desiredHeight, ImageFormat.JPEG, 1)
                        imgReader.setOnImageAvailableListener({ reader ->
                            val cameraImage = reader.acquireNextImage()

                            val buffer = cameraImage.planes.first().buffer
                            val bytes = ByteArray(buffer.capacity())
                            buffer.get(bytes)

                            val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.count(), null)
                            val frameToProcess = Frame.Builder().setBitmap(bitmap).build()
                            val barcodeResults = barcodeDetector.detect(frameToProcess)

                            if (barcodeResults.size() > 0) {
                                Log.d("Barcode:::", barcodeResults.valueAt(0).displayValue)
                                Toast.makeText(applicationContext, "Barcode detected!", Toast.LENGTH_SHORT).show()
                            } else {
                                Log.d("ERROR", "No barcode found")
                            }

                            cameraImage.close()
                        }, cameraBkgHandler)


                        val captureStateCallback = @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                        object : CameraCaptureSession.StateCallback() {
                            override fun onConfigured(session: CameraCaptureSession) {
                                val builder = camera.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)

                                builder.addTarget(scannerPreview.holder.surface)
                                builder.addTarget(imgReader.surface)
                                session.setRepeatingRequest(builder.build(), null, null)
                            }

                            override fun onConfigureFailed(session: CameraCaptureSession) {
                                // TODO
                                Log.d("ERROR", "onConfigureFailed")
                            }
                        }

                        camera.createCaptureSession(
                            listOf(scannerPreview.holder.surface, imgReader.surface),
                            captureStateCallback,
                            cameraBkgHandler
                        )
                    }

                    override fun onClosed(camera: CameraDevice) {
                        // TODO
                        Log.d("ERROR", "onClosed")
                    }

                    override fun onDisconnected(camera: CameraDevice) {
                        // TODO
                        Log.d("ERROR", "onDisconnected")
                    }

                    override fun onError(camera: CameraDevice, error: Int) {
                        // TODO
                        Log.d("ERROR", "onError")
                    }
                }

                cameraManager.openCamera(it, cameraStateCallback, cameraBkgHandler)
                return
            }

            // TODO: - No available camera found case

        } catch (e: CameraAccessException) {
            // TODO
            Log.e("ERROR", e.message)
        } catch (e: SecurityException) {
            // TODO
            Log.e("ERROR", e.message)
        }
    }


}
