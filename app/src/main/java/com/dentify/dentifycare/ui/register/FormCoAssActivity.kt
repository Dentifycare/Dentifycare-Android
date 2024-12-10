package com.dentify.dentifycare.ui.register

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import com.dentify.dentifycare.R
import com.dentify.dentifycare.databinding.ActivityFormCoAssBinding
import com.dentify.dentifycare.ui.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class FormCoAssActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFormCoAssBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormCoAssBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        auth = FirebaseAuth.getInstance()

        @Suppress("DEPRECATION")
        window.statusBarColor = getColor(R.color.dentifycare_main_color)

        getData()

        resultValidate()

        binding.btnRegister.setOnClickListener {
            if (validateForm()) {
                registerCoAss()
            } else {
                Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getData() {
        val email = intent.getStringExtra("EMAIL")
        val password = intent.getStringExtra("PASSWORD")
        val name = intent.getStringExtra("NAME")
        val phone = intent.getStringExtra("PHONE")

        binding.emailEditText.setText(email)
        binding.passwordEditText.setText(password)
        binding.nameEditText.setText(name)
        binding.phoneEditText.setText(phone)
    }

    private fun resultValidate() {
        binding.emailEditText.addTextChangedListener { validateForm() }
        binding.passwordEditText.addTextChangedListener { validateForm() }
        binding.nameEditText.addTextChangedListener { validateForm() }
        binding.phoneEditText.addTextChangedListener { validateForm() }
        binding.universityEditText.addTextChangedListener { validateForm() }
        binding.semesterEditText.addTextChangedListener { validateForm() }
        binding.studentEditText.addTextChangedListener { validateForm() }
    }

    private fun validateForm(): Boolean {
        val email = binding.emailEditText.text.toString().trim()
        val password = binding.passwordEditText.text.toString().trim()
        val name = binding.nameEditText.text.toString().trim()
        val phone = binding.phoneEditText.text.toString().trim()
        val university = binding.universityEditText.text.toString().trim()
        val semester = binding.semesterEditText.text.toString().trim()
        val studentId = binding.studentEditText.text.toString().trim()

        val isFormValid = email.isNotEmpty() &&
                password.isNotEmpty() &&
                name.isNotEmpty() &&
                phone.isNotEmpty() &&
                university.isNotEmpty() &&
                semester.isNotEmpty() &&
                studentId.isNotEmpty()

        binding.btnRegister.isEnabled = isFormValid

        return isFormValid
    }

    private fun registerCoAss() {
        val email = binding.emailEditText.text.toString().trim()
        val password = binding.passwordEditText.text.toString().trim()
        val name = binding.nameEditText.text.toString().trim()
        val phone = binding.phoneEditText.text.toString().trim()
        val university = binding.universityEditText.text.toString().trim()
        val semester = binding.semesterEditText.text.toString().trim()
        val studentId = binding.studentEditText.text.toString().trim()
        val role = "CoAss"

        binding.progressBar.visibility = View.VISIBLE

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid
                    val db = FirebaseFirestore.getInstance()

                    val userData = hashMapOf(
                        "name" to name,
                        "email" to email,
                        "phone" to phone,
                        "university" to university,
                        "semester" to semester,
                        "studentId" to studentId,
                        "role" to role
                    )

                    if (userId != null) {
                        db.collection("users").document(userId).set(userData)
                            .addOnSuccessListener {
                                binding.progressBar.visibility = View.GONE
                                Toast.makeText(this, "Registration Succeed!", Toast.LENGTH_SHORT).show()
                                navigationToLogin()
                            }
                            .addOnFailureListener { e ->
                                val errorMessage = "Failed to save data: ${e.message}"
                                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                            }

                    }
                } else {
                    val errorMessage = task.exception?.message ?: "Registration Failed!"
                    Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                }
            }

            .addOnFailureListener {
                binding.progressBar.visibility = View.GONE
                val errorMessage = it.message ?: "Registration Failed!"
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
            }
    }

    private fun navigationToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}