package com.dentify.dentifycare.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.dentify.dentifycare.databinding.FragmentHomeBinding
import com.dentify.dentifycare.ui.home.scan.ScanActivity

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnReadNews.setOnClickListener {
            navigationToNews()
        }
        binding.btnTryNow.setOnClickListener {
            navigationToScan()
        }
    }

    private fun navigationToNews() {
        val intent = Intent(requireContext(), NewsActivity::class.java)
        startActivity(intent)
    }

    private fun navigationToScan() {
        val intent = Intent(requireContext(), ScanActivity::class.java)
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}