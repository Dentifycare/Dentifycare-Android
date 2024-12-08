package com.dentify.dentifycare.ui.home.booked

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.dentify.dentifycare.R
import com.dentify.dentifycare.databinding.ActivityDetailBookedBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class DetailBookedActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBookedBinding

    private var name: String? = null
    private var hospital: String? = null
    private var skill: String? = null
    private var operationalDate: String? = null
    private var operationalHours: String? = null
    private var remainingQuota: String? = null
    private var additionalInformation: String? = null
    private var phone: String? = null
    private var phoneCoAss: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBookedBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        getData()

        binding.btnBook.setOnClickListener {
            navigationToConfirmationBooked()
        }
    }

    private fun getData() {
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            val uid = user.uid

            val db = FirebaseFirestore.getInstance()

            db.collection("users").document(uid).get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val role = document.getString("role")

                        name = intent.getStringExtra("EXTRA_NAME")
                        hospital = intent.getStringExtra("EXTRA_HOSPITAL")
                        skill = intent.getStringExtra("EXTRA_SKILL")
                        operationalDate = intent.getStringExtra("EXTRA_OPERATIONAL_DATE")
                        operationalHours = intent.getStringExtra("EXTRA_OPERATIONAL_HOURS")
                        remainingQuota = intent.getStringExtra("EXTRA_REMAINING_QUOTA")
                        additionalInformation = intent.getStringExtra("EXTRA_ADDITIONAL_INFORMATION")
                        phone = intent.getStringExtra("EXTRA_PHONE")

                        binding.tvNameCoAss.text = name
                        binding.tvTitleHospital.text = hospital
                        binding.tvSkill.text = skill
                        binding.additionalInformationValue.text = additionalInformation
                        binding.bookingDated.text = operationalDate
                        binding.bookingHours.text = operationalHours
                        binding.remainingQuotaValue.text = remainingQuota

                        if (role == "CoAss") {
                            binding.btnBook.visibility = View.GONE
                        }
                    }
                }
        }
    }


    private fun navigationToConfirmationBooked() {
        val intent = Intent(this, ConfirmationBookedActivity::class.java).apply {
            putExtra("EXTRA_NAME_Co_Ass", name)
            putExtra("EXTRA_HOSPITAL", hospital)
            putExtra("EXTRA_SKILL", skill)
            putExtra("EXTRA_OPERATIONAL_DATE", operationalDate)
            putExtra("EXTRA_OPERATIONAL_HOURS", operationalHours)
            putExtra("EXTRA_REMAINING_QUOTA", remainingQuota)
            putExtra("EXTRA_PHONE", phone)
            phoneCoAss = intent.getStringExtra("EXTRA_PHONE_Co_Ass")
            putExtra("EXTRA_Phone_Co_Ass", phoneCoAss)
            Log.d("CEKPHONE", phoneCoAss ?: "")
        }
        startActivity(intent)
    }
}