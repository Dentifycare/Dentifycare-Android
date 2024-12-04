package com.dentify.dentifycare.ui.register

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.dentify.dentifycare.R
import com.dentify.dentifycare.databinding.ActivityFormCoAssBinding
import com.dentify.dentifycare.ui.login.LoginActivity

class FormCoAssActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFormCoAssBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormCoAssBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        @Suppress("DEPRECATION")
        window.statusBarColor = getColor(R.color.dentifycare_main_color)

        binding.btnRegister.setOnClickListener {
            navigationToLogin()
        }
    }

    private fun navigationToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}