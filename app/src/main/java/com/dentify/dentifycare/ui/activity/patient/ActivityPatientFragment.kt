package com.dentify.dentifycare.ui.activity.patient

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.dentify.dentifycare.adapter.HistoryPatientAdapter
import com.dentify.dentifycare.data.local.HistoryPatient
import com.dentify.dentifycare.databinding.FragmentActivityPatientBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ActivityPatientFragment : Fragment() {
    private var _binding: FragmentActivityPatientBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: HistoryPatientAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentActivityPatientBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = HistoryPatientAdapter((emptyList()))
        binding.rvListActivity.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@ActivityPatientFragment.adapter
        }

        fetchPatientHistory()
    }

    private fun fetchPatientHistory() {
        binding.progressBar.visibility = View.VISIBLE
        val user = FirebaseAuth.getInstance().currentUser
        val idUser = user?.uid ?: return

        val db = FirebaseFirestore.getInstance()
        db.collection("history")
            .whereEqualTo("idUser", idUser)
            .get()
            .addOnSuccessListener { documents ->
                val historyList = mutableListOf<HistoryPatient>()
                for (document in documents) {
                    val history = HistoryPatient(
                        nameCoAss = document.getString("nameCoAss"),
                        hospital = document.getString("hospital"),
                        status = document.getString("status"),
                        historyID = document.getString("historyID")
                    )
                    historyList.add(history)
                }
                adapter.updateData(historyList)
                binding.progressBar.visibility = View.GONE
            }
            .addOnFailureListener { exception ->
                exception.printStackTrace()
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        fetchPatientHistory()
    }
}