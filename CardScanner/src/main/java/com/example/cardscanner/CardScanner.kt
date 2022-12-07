package com.example.cardscanner

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.DialogFragment
import com.example.cardscanner.databinding.CustomScannerBinding
import com.example.cardscanner.scanner.CardDetails
import com.example.cardscanner.scanner.CardType
import com.example.cardscanner.scanner.CreditCardDetails
import com.example.cardscanner.scanner.PermissionDeniedDialog
import com.example.cardscanner.util.*
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


// initialization
// functionality - scan + enter details
// set up func - logo + permanently denied text + enter details manually click action
// performance analyze

class CardScanner(private val cardDetails: CreditCardDetails) :
    DialogFragment(R.layout.custom_scanner) {

    private var cameraExecutor: ExecutorService = Executors.newSingleThreadExecutor()

    private var imageCapture: ImageCapture? = null

    private lateinit var binding: CustomScannerBinding

    private var cardType: CardType? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        return Dialog(requireContext(), R.style.AppTheme_FullScreenDialog)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        binding = CustomScannerBinding.bind(view)

         initEditTextFields()

         requestCameraPermission()

         setOnClickListener()

    }

    private fun initEditTextFields() {

        with(binding) {

            etCardNumber.clearError(tilCardNumber)

            etExpiryDate.clearError(tilExpiryDate)

        }
    }

    private fun requestCameraPermission() {

        requestPermissionLauncherForCameraPermission.launch(Manifest.permission.CAMERA)

    }

    private val requestPermissionLauncherForCameraPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->

            if (isGranted) initializeCamera()
            else {

                if (!ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),
                        Manifest.permission.CAMERA)
                ) {

                    PermissionDeniedDialog({

                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        intent.addCategory("android.intent.category.DEFAULT")
                        intent.data = Uri.parse("package:${requireContext().packageName}")
                        cameraResultListener.launch(intent)

                    }, {

                        binding.tvEnterDetailsManually.performClick()

                    }).show(requireActivity().supportFragmentManager, "Permission_denied_dialog")


                }

            }

        }

    private val cameraResultListener =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) initializeCamera()
            else requestCameraPermission()
        }


    private fun setOnClickListener() {

        with(binding) {

            closeIcon.setOnClickListener { dismiss() }

            tvEnterDetailsManually.setOnClickListener { setUpViewsForManualEntry() }

            btnContinue.setOnClickListener {

                if (isValidCardInfo()) {

                    cardDetails.cardDetails((CardDetails(cardNumber = etCardNumber.getTextFromTextView(),
                        expiryDate = etExpiryDate.getTextFromTextView(),
                        cardType = cardType?.cardType ?: "Unknown")))

                    dismiss()
                }

            }

            etCardNumber.doOnTextChanged { _, _, _, _ ->

                etCardNumber.setCreditCard { cardType = it }

            }

            etExpiryDate.setExpiryDate()

            etExpiryDate.setOnKeyListener { _, actionId, _ ->

                if (actionId == EditorInfo.IME_ACTION_DONE) {

                    btnContinue.performClick()

                }

                return@setOnKeyListener false
            }

        }
    }

    private fun isValidCardInfo(): Boolean {

        var isValid = true

        binding.apply {

            if (!etCardNumber.isValidString() || etCardNumber.getTextFromTextView().length < 10) {
                tilCardNumber.setErrorMessage("Invalid card number")
                isValid = false
            }

            if (!etExpiryDate.isValidExpiryDate()) {
                tilExpiryDate.setErrorMessage("Invalid expiry date")
                isValid = false
            }

        }

        return isValid
    }

    private fun initializeCamera() {

        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener({

            val preview = Preview.Builder().build()

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            val imageAnalyzer = ImageAnalysis.Builder().build()

            preview.setSurfaceProvider(binding.viewFinder.surfaceProvider)

            imageCapture = ImageCapture.Builder().build()

            imageAnalyzer.setAnalyzer(cameraExecutor) { imageProxy: ImageProxy? ->

                CoroutineScope(Dispatchers.Main).launch {
                    binding.progressBar.visibility = View.GONE
                }

                imageProxy.getTextFromScannedImage { cardDetailsFromApi ->

                    cardDetails.cardDetails(cardDetailsFromApi)

                    dismiss()

                }
            }

            try {

                val cameraProvider = cameraProviderFuture.get()

                cameraProvider.unbindAll()

                cameraProvider.bindToLifecycle(this,
                    cameraSelector,
                    preview,
                    imageCapture,
                    imageAnalyzer)

            } catch (e: java.lang.Exception) {

                e.printStackTrace()

                Log.i(ERROR_TAG, e.localizedMessage ?: e.message ?: "Camera initialization failed")

            }

        }, ContextCompat.getMainExecutor(requireContext()))

    }

    private fun setUpViewsForManualEntry() {

        with(binding) {
            //scanFields
            imgLogo.visibility = View.GONE
            tvScanCard.visibility = View.GONE
            cameraCard.visibility = View.GONE
            tvPermission.visibility = View.GONE
            tvEnterDetailsManually.visibility = View.GONE
            tvHelpScanCard.visibility = View.GONE
            //manualEntryFields
            tilCardNumber.visibility = View.VISIBLE
            tilExpiryDate.visibility = View.VISIBLE
            tvManualEntryInfo.visibility = View.VISIBLE
            tvCardDetails.visibility = View.VISIBLE
            btnContinue.visibility = View.VISIBLE
        }
    }

    private fun ImageProxy?.getTextFromScannedImage(cardDetails: (cardDetails: CardDetails) -> Unit) {

        Handler(requireContext().mainLooper).post {

            binding.viewFinder.bitmap?.let { bitmap ->

                val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

                val image = InputImage.fromBitmap(bitmap.scaleImage(), 0)

                recognizer.process(image)

                    .addOnSuccessListener { result: Text? ->
                        result.setValuesFromVisionText { cardNumber, expiryDate, cardType ->
                            Log.i(INFO_TAG, "Get card details successfully!")
                            cardDetails(CardDetails(cardNumber, expiryDate, cardType))
                        }
                    }

                    .addOnFailureListener { exception ->
                        Log.e(ERROR_TAG, exception.message.setErrorMessage())
                        Log.e(ERROR_TAG, exception.localizedMessage.setErrorMessage())
                        Log.i(INFO_TAG,
                            "Download library failed, make sure your testing device or emulator has google playstore.")
                    }

                    .addOnCompleteListener { this?.close() }
            }

        }

    }

}