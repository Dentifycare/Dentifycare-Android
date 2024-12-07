package com.dentify.dentifycare.ui.activity.coass

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.dentify.dentifycare.R
import com.dentify.dentifycare.databinding.ActivityPostBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

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

        binding.btnSubmit.setOnClickListener {
            uploadPost()
        }
    }

    private fun uploadPost() {
        val user = FirebaseAuth.getInstance().currentUser

        if (user == null) {
            Toast.makeText(this, "User not authenticated!", Toast.LENGTH_SHORT).show()
            return
        }

        val userRole = getUserRole(user.uid)

        if (userRole != "CoAss") {
            Toast.makeText(this, "You are not a CoAss!", Toast.LENGTH_SHORT).show()
            return
        }

        val name = binding.nameEditText.text.toString()
        val hospital = binding.hospitalEditText.text.toString()
        val city = binding.cityEditText.text.toString()
        val province = binding.provinceEditText.text.toString()
        val quota = binding.quotaEditText.text.toString()
        val additionalInfo = binding.informationEditText.text.toString()

        val selectedSkills = mutableListOf<String>()
        if (binding.rbCarries.isChecked) selectedSkills.add("carries")
        if (binding.rbCalculus.isChecked) selectedSkills.add("calculus")
        if (binding.rbGingivitis.isChecked) selectedSkills.add("gingivitis")
        if (binding.rbUlcers.isChecked) selectedSkills.add("ulcers")
        if (binding.rbHypodontia.isChecked) selectedSkills.add("hypodontia")
        if (binding.rbToothDiscoloration.isChecked) selectedSkills.add("tooth_discoloration")

        val postData = hashMapOf(
            "name" to name,
            "hospital" to hospital,
            "city" to city,
            "province" to province,
            "quota" to quota,
            "additionalInfo" to additionalInfo,
            "selectedSkills" to selectedSkills
        )

        db.collection("posts").add(postData)
            .addOnSuccessListener {
                Toast.makeText(this, "Post uploaded successfully!", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to upload post!", Toast.LENGTH_SHORT).show()
            }
    }

    private fun getUserRole(userId: String): String {
        var role = "none"
        val dbUser = db.collection("users").document(userId)

        dbUser.get().addOnSuccessListener { document ->
            if (document.exists()) {
                role = document.getString("role") ?: "none"
            }
        }
        return role
    }
}