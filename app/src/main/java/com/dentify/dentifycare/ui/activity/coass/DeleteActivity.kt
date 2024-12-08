package com.dentify.dentifycare.ui.activity.coass

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.dentify.dentifycare.R
import com.dentify.dentifycare.databinding.ActivityDeleteBinding

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
    }

    private fun callData() {
        val name = intent.getStringExtra("EXTRA_NAME")
        val hospital = intent.getStringExtra("EXTRA_HOSPITAL")
        val skill = intent.getStringExtra("EXTRA_SKILL")
        val additionalInformation = intent.getStringExtra("EXTRA_ADDITIONAL_INFORMATION")
        val operationalDate = intent.getStringExtra("EXTRA_OPERATIONAL_DATE")
        val operationalHours = intent.getStringExtra("EXTRA_OPERATIONAL_HOURS")
        val remainingQuota = intent.getStringExtra("EXTRA_REMAINING_QUOTA")
        val quota = intent.getStringExtra("EXTRA_QUOTA")

        binding.tvNameCoAss.text = name
        binding.tvTitleHospital.text = hospital
        binding.tvSkill.text = skill
        binding.additionalInformationValue.text = additionalInformation
        binding.bookingDated.text = operationalDate
        binding.bookingHours.text = operationalHours
        binding.remainingQuotaValue.text = remainingQuota
        binding.quotaValue.text = quota

    }
}