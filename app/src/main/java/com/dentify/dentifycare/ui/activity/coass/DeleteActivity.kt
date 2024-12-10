package com.dentify.dentifycare.ui.activity.coass

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.dentify.dentifycare.R
import com.dentify.dentifycare.databinding.ActivityDeleteBinding
import com.google.firebase.firestore.FirebaseFirestore

class DeleteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDeleteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeleteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        callData()

        binding.btnComplete.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
            val postId = intent.getStringExtra("EXTRA_POST_ID")
            val db = FirebaseFirestore.getInstance()
            db.collection("posts")
                .whereEqualTo("postId", postId)
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        db.collection("posts").document(document.id)
                            .update("status", "Completed")
                            .addOnSuccessListener {
                                Toast.makeText(this, "Status Completed", Toast.LENGTH_SHORT)
                                    .show()
                                finish()
                                binding.progressBar.visibility = View.GONE
                            }
                            .addOnFailureListener { e ->
                                e.printStackTrace()
                                Toast.makeText(this, "Failed to update status.", Toast.LENGTH_SHORT)
                                    .show()
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
    }

    private fun callData() {
        val name = intent.getStringExtra("EXTRA_NAME")
        val hospital = intent.getStringExtra("EXTRA_HOSPITAL")
        val skill = intent.getStringExtra("EXTRA_SKILL")
        val additionalInformation = intent.getStringExtra("EXTRA_ADDITIONAL_INFORMATION")
        val operationalDate = intent.getStringExtra("EXTRA_OPERATIONAL_DATE")
        val operationalHours = intent.getStringExtra("EXTRA_OPERATIONAL_HOURS")

        binding.tvNameCoAss.text = name
        binding.tvTitleHospital.text = hospital
        binding.tvSkill.text = skill
        binding.additionalInformationValue.text = additionalInformation
        binding.bookingDated.text = operationalDate
        binding.bookingHours.text = operationalHours

    }
}