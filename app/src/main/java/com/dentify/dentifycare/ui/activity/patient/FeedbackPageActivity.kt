package com.dentify.dentifycare.ui.activity.patient

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.dentify.dentifycare.R
import com.dentify.dentifycare.databinding.ActivityFeedbackPageBinding
import com.google.firebase.firestore.FirebaseFirestore

class FeedbackPageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFeedbackPageBinding
    private var isRatingGiven = false

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
            if (rating > 0) {
                isRatingGiven = true
            }
        }

        binding.btnSubmit.setOnClickListener {
            if (isRatingGiven) {
                submitRating()
            } else {
                Toast.makeText(this, "Please give a rating before submitting.", Toast.LENGTH_SHORT).show()
            }
        }

        getData()
    }

    private fun submitRating() {
        binding.progressBar.visibility = View.VISIBLE
        val historyId = intent.getStringExtra("EXTRA_HISTORY_ID")
        val db = FirebaseFirestore.getInstance()
        db.collection("history")
            .whereEqualTo("historyID", historyId)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    db.collection("history").document(document.id)
                        .update("status", "Completed")
                        .addOnSuccessListener {
                            Toast.makeText(this, "Feedback submitted.", Toast.LENGTH_SHORT).show()
                            finish()
                            binding.progressBar.visibility = View.GONE
                        }
                        .addOnFailureListener { e ->
                            e.printStackTrace()
                            Toast.makeText(this, "Failed to update status.", Toast.LENGTH_SHORT).show()
                            binding.progressBar.visibility = View.GONE
                        }
                }
            }
            .addOnFailureListener { e ->
                e.printStackTrace()
                Toast.makeText(this, "Failed to fetch document.", Toast.LENGTH_SHORT).show()
                binding.progressBar.visibility = View.GONE
            }
    }

    private fun getData() {
        val nameCoAss = intent.getStringExtra("EXTRA_NAME_Co_Ass")
        val hospital = intent.getStringExtra("EXTRA_HOSPITAL")

        binding.tvNameCoAss.text = nameCoAss
        binding.tvTitleHospital.text = hospital
    }
}