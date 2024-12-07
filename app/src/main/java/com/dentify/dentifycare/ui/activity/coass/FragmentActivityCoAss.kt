package com.dentify.dentifycare.ui.activity.coass

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.dentify.dentifycare.databinding.FragmentActivityCoAssBinding

class FragmentActivityCoAss : Fragment() {
    private var _binding: FragmentActivityCoAssBinding? = null
    private val binding get() = _binding!!

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
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fabPost.setOnClickListener {
            addPostCoAss()
        }
    }

    private fun addPostCoAss() {
        val intent = Intent(requireContext(), PostActivity::class.java)
        startActivity(intent)
    }
}