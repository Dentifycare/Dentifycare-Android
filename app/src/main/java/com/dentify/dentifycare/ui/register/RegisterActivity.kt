package com.dentify.dentifycare.ui.register

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.dentify.dentifycare.R
import com.dentify.dentifycare.databinding.ActivityRegisterBinding
import com.dentify.dentifycare.ui.login.LoginActivity

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        @Suppress("DEPRECATION")
        window.statusBarColor = getColor(R.color.dentifycare_main_color)

        binding.btnNext.setOnClickListener {
            handleNavigation()
        }

        handleNavigation()

        textButton()
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

    private fun handleNavigation() {
        val selectedRoleId = binding.roleUserGroup.checkedRadioButtonId
        when (selectedRoleId) {
            R.id.radio_coass -> {
                val intent = Intent(this,FormCoAssActivity::class.java)
                startActivity(intent)
            }
            R.id.radio_patient -> {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
            else -> {
                Toast.makeText(this, "Please select a role", Toast.LENGTH_SHORT).show()
            }
        }
    }
}