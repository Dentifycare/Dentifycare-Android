package com.dentify.dentifycare.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.dentify.dentifycare.R
import com.dentify.dentifycare.ui.home.FormCoAssActivity
import com.dentify.dentifycare.ui.home.HomeFragment

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun navigationToHomeActivity() {
        val intent = Intent(this, HomeFragment::class.java)
        startActivity(intent)
        finish()
    }

    private fun navigationToFormCoAss() {
        val intent = Intent(this, FormCoAssActivity::class.java)
        startActivity(intent)
        finish()
    }
}