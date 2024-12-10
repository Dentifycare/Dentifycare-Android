package com.dentify.dentifycare.ui.activity.coass

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import com.dentify.dentifycare.R
import com.dentify.dentifycare.databinding.ActivityPostBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class PostActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPostBinding
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        resultValidation()

        binding.btnSubmit.setOnClickListener {
            if (validateForm()) {
                uploadPost()
            } else {
                Toast.makeText(this, "Please fill in all required fields!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun resultValidation() {
        binding.nameEditText.addTextChangedListener { validateForm() }
        binding.hospitalEditText.addTextChangedListener { validateForm() }
        binding.cityEditText.addTextChangedListener { validateForm() }
        binding.provinceEditText.addTextChangedListener { validateForm() }
        binding.quotaEditText.addTextChangedListener { validateForm() }
        binding.informationEditText.addTextChangedListener { validateForm() }
    }

    private fun validateForm(): Boolean {
        val name = binding.nameEditText.text.toString().trim()
        val hospital = binding.hospitalEditText.text.toString().trim()
        val city = binding.cityEditText.text.toString().trim()
        val province = binding.provinceEditText.text.toString().trim()
        val quota = binding.quotaEditText.text.toString().trim()
        val additionalInfo = binding.informationEditText.text.toString().trim()

        val isFormValid = name.isNotEmpty() && hospital.isNotEmpty() && city.isNotEmpty() &&
                province.isNotEmpty() && quota.isNotEmpty() && additionalInfo.isNotEmpty()

        binding.btnSubmit.isEnabled = isFormValid
        return isFormValid
    }

    private fun uploadPost() {
        binding.progressBar.visibility = View.VISIBLE
        val user = FirebaseAuth.getInstance().currentUser

        if (user == null) {
            Toast.makeText(this, "User not authenticated!", Toast.LENGTH_SHORT).show()
            return
        }

        val email = user.email
        val uid = user.uid
        val status = "Uncompleted"
        val name = binding.nameEditText.text.toString()
        val hospital = binding.hospitalEditText.text.toString()
        val city = binding.cityEditText.text.toString()
        val province = binding.provinceEditText.text.toString()
        val quota = binding.quotaEditText.text.toString()
        val additionalInfo = binding.informationEditText.text.toString()
        val currentDate = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault()).format(Date())

        val selectedSkills = mutableListOf<String>()
        if (binding.rbCarries.isChecked) selectedSkills.add("caries")
        if (binding.rbCalculus.isChecked) selectedSkills.add("calculus")
        if (binding.rbGingivitis.isChecked) selectedSkills.add("gingivitis")
        if (binding.rbUlcers.isChecked) selectedSkills.add("ulcers")
        if (binding.rbHypodontia.isChecked) selectedSkills.add("hypodontia")
        if (binding.rbToothDiscoloration.isChecked) selectedSkills.add("tooth discoloration")

        val selectedHours = mutableListOf<String>()
        if (binding.rbHours1.isChecked) selectedHours.add(getString(R.string.example_hours))
        if (binding.rbHours2.isChecked) selectedHours.add(getString(R.string.example_hours_2))
        if (binding.rbHours3.isChecked) selectedHours.add(getString(R.string.example_hours_3))


        val userRef = db.collection("users").document(uid)
        userRef.get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val phoneNumber = document.getString("phone")

                    val postId = UUID.randomUUID().toString()
                    val postData = hashMapOf(
                        "postId" to postId,
                        "email" to email,
                        "uid" to uid,
                        "phone" to phoneNumber,
                        "status" to status,
                        "name" to name,
                        "hospital" to hospital,
                        "city" to city,
                        "province" to province,
                        "quota" to quota,
                        "additionalInfo" to additionalInfo,
                        "currentDate" to currentDate,
                        "selectedSkills" to selectedSkills,
                        "selectedHours" to selectedHours
                    )

                    db.collection("posts").add(postData)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Post uploaded successfully!", Toast.LENGTH_SHORT).show()
                            binding.progressBar.visibility = View.GONE
                            finish()
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Failed to upload post!", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    Toast.makeText(this, "User data not found!", Toast.LENGTH_SHORT).show()
                    binding.progressBar.visibility = View.GONE
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to get user phone number!", Toast.LENGTH_SHORT).show()
                binding.progressBar.visibility = View.GONE
            }
    }

}