package com.dentify.dentifycare.ui.home.scan

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.dentify.dentifycare.R
import com.dentify.dentifycare.data.remote.response.PredictResponse
import com.dentify.dentifycare.databinding.ActivityScanBinding
import com.dentify.dentifycare.databinding.DialogUploadOptionsBinding
import com.dentify.dentifycare.ui.home.analyze.AnalyzeActivity
import com.dentify.dentifycare.utils.ImageHelper.getImageUri
import com.dentify.dentifycare.utils.ImageHelper.reduceFileImage
import com.dentify.dentifycare.utils.ImageHelper.uriToFile
import com.yalantis.ucrop.UCrop
import com.yalantis.ucrop.UCropActivity
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class ScanActivity : AppCompatActivity() {
    private lateinit var binding: ActivityScanBinding
    private val viewModel: ScanViewModel by viewModels()

    private var previousImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScanBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnUpload.setOnClickListener {
            showCustomUploadDialog()
        }

        binding.btnAnalyze.setOnClickListener {
            upload()
        }

        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.imageUri.observe(this) { uri ->
            if (uri != null) {
                Log.d("ScanActivity", "New Image URI: $uri")
                binding.imgAnalyze.setImageURI(null) // Reset cache
                binding.imgAnalyze.setImageURI(uri)
            } else {
                binding.imgAnalyze.setImageDrawable(
                    AppCompatResources.getDrawable(
                        this,
                        R.drawable.initiate_ai_image
                    )
                )
            }
        }
    }

    private fun showCustomUploadDialog() {
        val dialogView = DialogUploadOptionsBinding.inflate(layoutInflater)

        val builder = AlertDialog.Builder(this)
        builder.setView(dialogView.root)

        val dialog = builder.create()
        dialog.show()

        dialogView.btnGallery.setOnClickListener {
            startGallery()
            dialog.dismiss()
        }

        dialogView.btnCamera.setOnClickListener {
            startCamera()
            dialog.dismiss()
        }
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            startCrop(uri)
        } else {
            Toast.makeText(this, getString(R.string.no_gallery), Toast.LENGTH_SHORT).show()
        }
    }

    private fun startCamera() {
        val uri = getImageUri(this)
        previousImageUri = uri
        viewModel.setImageUri(uri)
        launcherIntentCamera.launch(uri)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            viewModel.imageUri.value?.let { uri ->
                Log.d("ScanActivity", "Camera URI: $uri")
                startCrop(uri)
            } ?: run {
                Toast.makeText(this, getString(R.string.no_image), Toast.LENGTH_SHORT).show()
            }
        } else {
            viewModel.setImageUri(previousImageUri)
            if (previousImageUri == null) {
                binding.imgAnalyze.setImageDrawable(
                    AppCompatResources.getDrawable(
                        this,
                        R.drawable.initiate_ai_image
                    )
                )
            }
            Toast.makeText(this, getString(R.string.no_image), Toast.LENGTH_SHORT).show()
        }
    }

    private fun startCrop(sourceUri: Uri) {
        val destinationUri = Uri.fromFile(File(cacheDir, "cropped_image.jpg"))

        val uCrop = UCrop.of(sourceUri, destinationUri)
            .withAspectRatio(1f, 1f)
            .withOptions(getCropOptions())

        Log.d("ScanActivity", "Starting crop with URI: $sourceUri")

        launcherCrop.launch(uCrop.getIntent(this))
    }

    private val launcherCrop = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val resultUri = UCrop.getOutput(result.data!!)
            if (resultUri != null) {
                viewModel.setImageUri(resultUri)
                binding.imgAnalyze.setImageURI(null)
                binding.imgAnalyze.setImageURI(resultUri)
                Log.d("ScanActivity", "Cropped URI: $resultUri")
            }
        } else {
            Toast.makeText(this, getString(R.string.crop_failed), Toast.LENGTH_SHORT).show()
        }
    }

    private fun getCropOptions(): UCrop.Options {
        val options = UCrop.Options()
        options.setCompressionQuality(80)
        options.setHideBottomControls(true)
        options.setFreeStyleCropEnabled(false)
        options.setAllowedGestures(UCropActivity.ALL, UCropActivity.NONE, UCropActivity.ALL)
        return options
    }

    private fun upload() {
        val uri = viewModel.imageUri.value
        if (uri != null) {
            val imageFile = uriToFile(uri, this).reduceFileImage()
            val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
            val multipartBody = MultipartBody.Part.createFormData(
                "file",
                imageFile.name,
                requestImageFile
            )

            viewModel.getPredict(this, multipartBody) { response ->
                if (response != null) {
                    navigationToAnalyze(response)
                } else {
                    Toast.makeText(this, "Upload Failed", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(this, getString(R.string.no_image_selected), Toast.LENGTH_SHORT).show()
        }
    }

    private fun navigationToAnalyze(response: PredictResponse) {
        val intent = Intent(this, AnalyzeActivity::class.java)
        intent.putExtra("EXTRA_IMAGE_URI", viewModel.imageUri.value.toString())
        intent.putExtra("EXTRA_DIAGNOSIS", response.diagnosis)
        intent.putExtra("EXTRA_ACCURACY", response.accuracy)
        startActivity(intent)
    }
}