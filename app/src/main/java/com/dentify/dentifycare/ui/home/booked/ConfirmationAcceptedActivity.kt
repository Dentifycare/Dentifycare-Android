package com.dentify.dentifycare.ui.home.booked

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.dentify.dentifycare.R
import com.dentify.dentifycare.databinding.ActivityConfirmationAcceptedBinding
import com.dentify.dentifycare.ui.MainActivity

class ConfirmationAcceptedActivity : AppCompatActivity() {
    private lateinit var binding: ActivityConfirmationAcceptedBinding

    private var phoneCoAss: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConfirmationAcceptedBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        getData()

        phoneCoAss = intent.getStringExtra("EXTRA_Phone_Co_Ass")

        binding.btnConfirmationNow.setOnClickListener {
            openWhatsApp()
        }
    }

    private fun getData() {
        val yourName = intent.getStringExtra("EXTRA_YOUR_NAME")
        val nameCoAss = intent.getStringExtra("EXTRA_NAME_Co_Ass")
        val hospital = intent.getStringExtra("EXTRA_HOSPITAL")
        val skill = intent.getStringExtra("EXTRA_SKILL")
        val operationalDate = intent.getStringExtra("EXTRA_OPERATIONAL_DATE")
        val operationalHours = intent.getStringExtra("EXTRA_OPERATIONAL_HOURS")
        val remainingQuota = intent.getStringExtra("EXTRA_REMAINING_QUOTA")

        binding.nameOrder.text = yourName
        binding.coAssName.text = nameCoAss
        binding.hospitalName.text = hospital
        binding.diagnosisName.text = skill
        binding.bookingDated.text = operationalDate
        binding.bookingHours.text = operationalHours
        binding.queue.text = remainingQuota

    }

    private fun openWhatsApp() {
        val yourName = intent.getStringExtra("EXTRA_YOUR_NAME") ?: "Unknown"
        val nameCoAss = intent.getStringExtra("EXTRA_NAME_Co_Ass") ?: "Unknown"
        val hospital = intent.getStringExtra("EXTRA_HOSPITAL") ?: "Unknown"
        val skill = intent.getStringExtra("EXTRA_SKILL") ?: "Unknown"
        val operationalDate = intent.getStringExtra("EXTRA_OPERATIONAL_DATE") ?: "Unknown"
        val operationalHours = intent.getStringExtra("EXTRA_OPERATIONAL_HOURS") ?: "Unknown"
        val remainingQuota = intent.getStringExtra("EXTRA_REMAINING_QUOTA") ?: "Unknown"

        val message = "Hello, my name is $yourName.\n" +
                "I have booked a session with Co-Assistant $nameCoAss.\n" +
                "Location: $hospital.\n" +
                "Skill: $skill.\n" +
                "Booking Date: $operationalDate.\n" +
                "Operational Hours: $operationalHours.\n" +
                "Remaining Quota: $remainingQuota.\n" +
                "Looking forward to connecting with you!"

        val phoneNumber = phoneCoAss

        val url = "https://wa.me/$phoneNumber?text=${Uri.encode(message)}"

        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(url)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }

        try {
            startActivity(intent)
            Handler(Looper.getMainLooper()).postDelayed({
                val mainIntent = Intent(this, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                }
                startActivity(mainIntent)
                finish()
            }, 5000)

        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, "WhatsApp Not Found!", Toast.LENGTH_SHORT).show()
        }
    }
}