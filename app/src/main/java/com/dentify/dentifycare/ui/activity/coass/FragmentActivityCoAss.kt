package com.dentify.dentifycare.ui.activity.coass

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.dentify.dentifycare.adapter.ActivityCoAssAdapter
import com.dentify.dentifycare.data.local.PostCoAss
import com.dentify.dentifycare.databinding.FragmentActivityCoAssBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

class FragmentActivityCoAss : Fragment() {
    private var _binding: FragmentActivityCoAssBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: ActivityCoAssAdapter

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private var listenerRegistration: ListenerRegistration? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentActivityCoAssBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        listenerRegistration?.remove()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        fetchData()

        binding.fabPost.setOnClickListener {
            addPostCoAss()
        }
    }

    private fun setupRecyclerView() {
        adapter = ActivityCoAssAdapter(emptyList())
        binding.rvListActivity.layoutManager = LinearLayoutManager(requireContext())
        binding.rvListActivity.adapter = adapter
    }

    private fun fetchData() {
        binding.progressBar.visibility = View.VISIBLE
        val currentUser = auth.currentUser

        if (currentUser != null) {
            val userUid = currentUser.uid

            listenerRegistration = db.collection("posts")
                .whereEqualTo("uid", userUid)
                .addSnapshotListener { snapshot, error ->
                    binding.progressBar.visibility = View.GONE

                    if (error != null) {
                        return@addSnapshotListener
                    }

                    if (snapshot != null && !snapshot.isEmpty) {
                        val postList = snapshot.documents.mapNotNull { doc ->
                            doc.toObject(PostCoAss::class.java)?.apply {
                                uid = doc.id
                            }
                        }
                        adapter.updateData(postList)
                    } else {
                        adapter.updateData(emptyList())
                    }
                }
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun addPostCoAss() {
        val intent = Intent(requireContext(), PostActivity::class.java)
        startActivity(intent)
    }
}