package com.dentify.dentifycare.ui.home.news

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dentify.dentifycare.adapter.NewsAdapter
import com.dentify.dentifycare.data.local.News
import com.dentify.dentifycare.databinding.ActivityNewsBinding
import com.google.firebase.firestore.FirebaseFirestore

class NewsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewsBinding
    private val db = FirebaseFirestore.getInstance()
    private lateinit var newsAdapter: NewsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        fetchNews()
    }

    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter(emptyList())
        binding.rvNewsList.apply {
            layoutManager = LinearLayoutManager(this@NewsActivity)
            adapter = newsAdapter
        }
    }

    private fun fetchNews() {
        binding.progressBar.visibility = View.VISIBLE
        db.collection("news")
            .get()
            .addOnSuccessListener { documents ->
                binding.progressBar.visibility = View.GONE
                val newsList = mutableListOf<News>()
                for (document in documents) {
                    Log.d("NewsActivity", "Berhasil mendapatkan berita: ${document.data}")
                    val news = document.toObject(News::class.java)
                    newsList.add(news)
                }
                newsAdapter.updateData(newsList)
            }
            .addOnFailureListener { exception ->
                Log.e("NewsActivity", "Error mendapatkan berita", exception)
            }
    }
}