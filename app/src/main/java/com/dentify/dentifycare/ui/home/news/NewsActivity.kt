package com.dentify.dentifycare.ui.home.news

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dentify.dentifycare.databinding.ActivityNewsBinding

class NewsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}