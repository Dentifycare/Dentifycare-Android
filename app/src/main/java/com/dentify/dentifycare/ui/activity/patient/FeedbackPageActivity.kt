package com.dentify.dentifycare.ui.activity.patient

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.dentify.dentifycare.R
import com.dentify.dentifycare.databinding.ActivityFeedbackPageBinding

class FeedbackPageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFeedbackPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFeedbackPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.ratingBar.setOnRatingBarChangeListener { _, rating, _ ->
            Toast.makeText(this, "Rating: $rating", Toast.LENGTH_SHORT).show()
        }

        binding.btnSubmit.setOnClickListener {
            finish()
        }

        getData()
    }

    private fun getData() {
        val nameCoAss = intent.getStringExtra("EXTRA_NAME_Co_Ass")
        val hospital = intent.getStringExtra("EXTRA_HOSPITAL")

        binding.tvNameCoAss.text = nameCoAss
        binding.tvTitleHospital.text = hospital
    }
}