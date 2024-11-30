package com.dentify.dentifycare.ui.home.scan

import android.net.Uri
import android.os.Bundle
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
import com.dentify.dentifycare.databinding.ActivityScanBinding
import com.dentify.dentifycare.databinding.DialogUploadOptionsBinding
import com.dentify.dentifycare.utils.ImageHelper.getImageUri

class ScanActivity : AppCompatActivity() {
    private lateinit var binding: ActivityScanBinding

    private val viewModel: ScanViewModel by viewModels()

    private var imageUri: Uri? = null

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

        setUpObserves()
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

    private fun setUpObserves() {
        viewModel.imageUri.observe(this) { uri ->
            if (uri != null) {
                binding.imgAnalyze.setImageURI(uri)
            }
        }
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            viewModel.setImageUri(uri)
            imageUri = uri
        } else {
            Toast.makeText(this, getString(R.string.no_gallery), Toast.LENGTH_SHORT).show()
        }
    }

    private fun startCamera() {
        val uri = getImageUri(this)
        viewModel.setImageUri(uri)
        launcherIntentCamera.launch(uri)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            viewModel.imageUri.value?.let { uri ->
                binding.imgAnalyze.setImageURI(uri)
            }
        } else {
            viewModel.setImageUri(imageUri)
            if (imageUri == null) {
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
}