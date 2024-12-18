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
import com.dentify.dentifycare.databinding.ActivityRegisterBinding
import com.dentify.dentifycare.ui.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        auth = FirebaseAuth.getInstance()

        @Suppress("DEPRECATION")
        window.statusBarColor = getColor(R.color.dentifycare_main_color)

        binding.btnNext.setOnClickListener {
            handleNavigation()
        }

        binding.tvLoginNow.setOnClickListener {
            loginNavigation()
        }

        textButton()

        resultValidate()
    }

    private fun resultValidate() {
        binding.emailEditText.addTextChangedListener { validateForm() }
        binding.passwordEditText.addTextChangedListener { validateForm() }
        binding.nameEditText.addTextChangedListener { validateForm() }
        binding.phoneEditText.addTextChangedListener { validateForm() }
    }

    private fun textButton() {
        binding.roleUserGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radio_coass -> {
                    binding.btnNext.text = getString(R.string.next)
                }
                R.id.radio_patient -> {
                    binding.btnNext.text = getString(R.string.register)
                }
            }
        }
    }

    private fun registerPatient() {
        val email = binding.emailEditText.text.toString().trim()
        val password = binding.passwordEditText.text.toString().trim()
        val name = binding.nameEditText.text.toString().trim()
        val phone = binding.phoneEditText.text.toString().trim()
        val role = "Patient"

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
                        "role" to role
                    )

                    if (userId != null) {
                        db.collection("users").document(userId).set(userData)
                            .addOnSuccessListener {
                                binding.progressBar.visibility = View.GONE
                                Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show()
                                loginNavigation()
                            }
                            .addOnFailureListener { e ->
                                val errorMessage = "Failed to save data: ${e.message}"
                                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                            }

                    }
                } else {
                    val errorMessage = task.exception?.message ?: "Registration failed!"
                    Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                binding.progressBar.visibility = View.GONE
                val errorMessage = it.message ?: "Registration failed!"
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
            }
    }

    private fun handleNavigation() {
        val selectedRoleId = binding.roleUserGroup.checkedRadioButtonId
        val email = binding.emailEditText.text.toString().trim()
        val password = binding.passwordEditText.text.toString().trim()
        val name = binding.nameEditText.text.toString().trim()
        val phone = binding.phoneEditText.text.toString().trim()

        val isFormValid = email.isNotEmpty() && password.isNotEmpty() && name.isNotEmpty() && phone.isNotEmpty()

        if (!isFormValid) {
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show()
            return
        }

        when (selectedRoleId) {
            R.id.radio_coass -> {
                val intent = Intent(this,FormCoAssActivity::class.java)
                intent.putExtra("EMAIL", email)
                intent.putExtra("PASSWORD", password)
                intent.putExtra("NAME", name)
                intent.putExtra("PHONE", phone)
                startActivity(intent)
            }
            R.id.radio_patient -> {
                registerPatient()
            }
            else -> {
                Toast.makeText(this, "Please select a role", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loginNavigation() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun validateForm() {
        val email = binding.emailEditText.text.toString().trim()
        val password = binding.passwordEditText.text.toString().trim()
        val name = binding.nameEditText.text.toString().trim()
        val phone = binding.phoneEditText.text.toString().trim()

        val isFormValid = email.isNotEmpty() && password.isNotEmpty() && name.isNotEmpty() && phone.isNotEmpty()
        binding.btnNext.isEnabled = isFormValid
    }
}