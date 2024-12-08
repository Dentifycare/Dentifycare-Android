package com.dentify.dentifycare.ui.home.analyze

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.dentify.dentifycare.R
import com.dentify.dentifycare.databinding.ActivityAnalyzeBinding

class AnalyzeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAnalyzeBinding
    private var diagnosis: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAnalyzeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnNext.setOnClickListener {
            navigationToAnalyzeFound()
        }

        getData()
    }

    private fun getData() {
        val imageUri = intent.getStringExtra("EXTRA_IMAGE_URI")
        diagnosis = intent.getStringExtra("EXTRA_DIAGNOSIS")
        val accuracy = intent.getStringExtra("EXTRA_ACCURACY")

        if (imageUri != null) {
            binding.imgResult.setImageURI(Uri.parse(imageUri))
        }

        binding.tvDiagnosisResult.text = diagnosis
        binding.tvAnalyzeResult.text = accuracy
    }

    private fun navigationToAnalyzeFound() {
        val intent = Intent(this, AnalyzeFoundActivity::class.java).apply {
            putExtra("EXTRA_DIAGNOSIS", diagnosis)
        }
        startActivity(intent)
    }
}