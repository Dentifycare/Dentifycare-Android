package com.dentify.dentifycare.ui.home.analyze

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.dentify.dentifycare.R
import com.dentify.dentifycare.adapter.CoAssAdapter
import com.dentify.dentifycare.data.local.CoAssBooked
import com.dentify.dentifycare.databinding.ActivityAnalyzeFoundBinding
import com.google.firebase.firestore.FirebaseFirestore

class AnalyzeFoundActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAnalyzeFoundBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var adapter: CoAssAdapter

    private var diagnosisFilter: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAnalyzeFoundBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        db = FirebaseFirestore.getInstance()

        adapter = CoAssAdapter(emptyList())
        binding.rvListActivity.layoutManager = LinearLayoutManager(this)
        binding.rvListActivity.adapter = adapter

        diagnosisFilter = intent.getStringExtra("EXTRA_DIAGNOSIS")
        fetchPosts()
    }

    private fun fetchPosts() {
        binding.progressBar.visibility = View.VISIBLE
        db.collection("posts")
            .get()
            .addOnSuccessListener { result ->
                val postList = mutableListOf<CoAssBooked>()
                for (document in result) {
                    val post = document.toObject(CoAssBooked::class.java).apply {
                        postId = document.id
                    }

                    if (post.status != "Completed" && (diagnosisFilter == null || post.selectedSkills[0] == diagnosisFilter)) {
                        postList.add(post)
                    }
                }
                adapter.updateData(postList)
                binding.progressBar.visibility = View.GONE
            }
            .addOnFailureListener { exception ->
                Log.e("AnalyzeActivity", "Error fetching posts", exception)
                Toast.makeText(this, "Failed to fetch posts", Toast.LENGTH_SHORT).show()
            }
    }
}