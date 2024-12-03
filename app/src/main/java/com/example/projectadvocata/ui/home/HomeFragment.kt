package com.example.projectadvocata.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projectadvocata.databinding.FragmentHomeBinding
import com.example.projectadvocata.ui.ViewModelFactory
import com.example.projectadvocata.ui.webview.UUD
import com.example.projectadvocata.ui.webview.UUKUHP
import kotlin.getValue

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel: HomeViewModel by viewModels {
        ViewModelFactory.getAuthInstance(
            requireActivity()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerViews()
        observeEvents()
        binding.buttonKUHP.setOnClickListener {
            val intent = Intent(requireContext(), UUKUHP::class.java)
            startActivity(intent)
        }
        binding.buttonUUD.setOnClickListener {
            val intent = Intent(requireContext(), UUD::class.java)
            startActivity(intent)
        }
    }

    private fun setupRecyclerViews() {
    }

    private fun observeEvents() {
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
