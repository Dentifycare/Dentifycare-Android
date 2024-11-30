package com.dentify.dentifycare.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.dentify.dentifycare.MainActivity
import com.dentify.dentifycare.R
import com.dentify.dentifycare.databinding.ActivityLoginBinding
import com.dentify.dentifycare.ui.register.FormCoAssActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        @Suppress("DEPRECATION")
        window.statusBarColor = getColor(R.color.dentifycare_main_color)

        binding.imgGoogle.setOnClickListener {
            navigationToHomeActivity()
        }
    }

    private fun navigationToHomeActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun navigationToFormCoAss() {
        val intent = Intent(this, FormCoAssActivity::class.java)
        startActivity(intent)
        finish()
    }
}