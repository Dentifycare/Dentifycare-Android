package com.dentify.dentifycare.ui.home.booked

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.dentify.dentifycare.R
import com.dentify.dentifycare.databinding.ActivityConfirmationBookedBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.UUID

class ConfirmationBookedActivity : AppCompatActivity() {
    private lateinit var binding: ActivityConfirmationBookedBinding

    private var yourName: String? = null
    private var nameCoAss: String? = null
    private var hospital: String? = null
    private var skill: String? = null
    private var operationalDate: String? = null
    private var operationalHours: String? = null
    private var phoneCoAss: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConfirmationBookedBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        getData()

        binding.btnConfirmationNow.setOnClickListener {
            navigateToConfirmationAccepted()
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
                        yourName = document.getString("name")
                        nameCoAss = intent.getStringExtra("EXTRA_NAME_Co_Ass")
                        hospital = intent.getStringExtra("EXTRA_HOSPITAL")
                        skill = intent.getStringExtra("EXTRA_SKILL")
                        operationalDate = intent.getStringExtra("EXTRA_OPERATIONAL_DATE")
                        operationalHours = intent.getStringExtra("EXTRA_OPERATIONAL_HOURS")

                        binding.nameOrder.text = yourName
                        binding.coAssName.text = nameCoAss
                        binding.hospitalName.text = hospital
                        binding.diagnosisName.text = skill
                        binding.bookingDated.text = operationalDate
                        binding.bookingHours.text = operationalHours
                    }
                }
        }
    }

    private fun navigateToConfirmationAccepted() {
        val db = FirebaseFirestore.getInstance()
        binding.progressBar.visibility = View.VISIBLE

        phoneCoAss = intent.getStringExtra("EXTRA_Phone_Co_Ass")
        val user = FirebaseAuth.getInstance().currentUser
        val idUser = user?.uid
        val status = "Uncompleted"
        val historyID = UUID.randomUUID().toString()
        val postId = intent.getStringExtra("EXTRA_POST_ID")

        val historyData = hashMapOf(
            "postId" to postId,
            "historyID" to historyID,
            "idUser" to idUser,
            "yourName" to yourName,
            "nameCoAss" to nameCoAss,
            "hospital" to hospital,
            "skill" to skill,
            "operationalDate" to operationalDate,
            "operationalHours" to operationalHours,
            "phoneCoAss" to phoneCoAss,
            "status" to status
        )

        db.collection("history").document()
            .set(historyData)
            .addOnSuccessListener {
                val intent = Intent(this, ConfirmationAcceptedActivity::class.java).apply {
                    putExtra("EXTRA_YOUR_NAME", yourName)
                    putExtra("EXTRA_NAME_Co_Ass", nameCoAss)
                    putExtra("EXTRA_HOSPITAL", hospital)
                    putExtra("EXTRA_SKILL", skill)
                    putExtra("EXTRA_OPERATIONAL_DATE", operationalDate)
                    putExtra("EXTRA_OPERATIONAL_HOURS", operationalHours)
                    phoneCoAss = intent.getStringExtra("EXTRA_Phone_Co_Ass")
                    putExtra("EXTRA_Phone_Co_Ass", phoneCoAss)

                    Toast.makeText(this@ConfirmationBookedActivity, "Booked Successfully", Toast.LENGTH_SHORT).show()
                    binding.progressBar.visibility = View.GONE
                }
                startActivity(intent)
            }
    }
}