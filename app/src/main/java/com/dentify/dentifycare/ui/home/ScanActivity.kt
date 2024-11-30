package com.dentify.dentifycare.ui.home

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.dentify.dentifycare.R
import com.dentify.dentifycare.databinding.ActivityScanBinding
import com.dentify.dentifycare.databinding.DialogUploadOptionsBinding

class ScanActivity : AppCompatActivity() {
    private lateinit var binding: ActivityScanBinding

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
    }

    private fun showCustomUploadDialog() {
        val dialogView = DialogUploadOptionsBinding.inflate(layoutInflater)

        val builder = AlertDialog.Builder(this)
        builder.setView(dialogView.root)

        val dialog = builder.create()
        dialog.show()

        dialogView.btnGallery.setOnClickListener {

        }

        dialogView.btnCamera.setOnClickListener {

        }
    }
}