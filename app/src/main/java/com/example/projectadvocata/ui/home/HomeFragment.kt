@file:Suppress("unused", "RedundantSuppression")

package com.example.projectadvocata.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projectadvocata.data.local.entity.EventEntity
import com.example.projectadvocata.data.repo.Result
import com.example.projectadvocata.databinding.FragmentHomeBinding
import com.example.projectadvocata.ViewModelFactory
import com.example.projectadvocata.ui.adapter.EventHorizontalAdapter
import com.example.projectadvocata.ui.adapter.EventVerticalAdapter
import com.example.projectadvocata.ui.detail.DetailActivity
import com.example.projectadvocata.ui.home.HomeViewModel

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel: HomeViewModel by viewModels {
        ViewModelFactory.getInstance(
            requireActivity()
        )
    }
    private lateinit var eventHorizontalAdapter: EventHorizontalAdapter
    private lateinit var eventVerticalAdapter: EventVerticalAdapter

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
    }

    private fun setupRecyclerViews() {
        eventHorizontalAdapter = EventHorizontalAdapter { navigateToDetailEvent(it) }
        binding.rvUpcoming.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = eventHorizontalAdapter
        }

        eventVerticalAdapter = EventVerticalAdapter { navigateToDetailEvent(it) }
        binding.rvEvent.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = eventVerticalAdapter
        }
    }

    private fun observeEvents() {
        homeViewModel.getFinishedEvents().observe(viewLifecycleOwner) { result ->
            handleEventResult(result, eventHorizontalAdapter)
        }

        homeViewModel.getFinishedEvents().observe(viewLifecycleOwner) { result ->
            handleEventResult(result, eventVerticalAdapter)
        }
    }

    private fun handleEventResult(
        result: Result<List<EventEntity>>,
        adapter: androidx.recyclerview.widget.ListAdapter<EventEntity, *>
    ) {
        when (result) {
            is Result.Loading -> {
                binding.tvNoEvent.visibility = View.GONE
                binding.progressBar.visibility = View.VISIBLE
                binding.progressBar2.visibility = View.VISIBLE
            }

            is Result.Success -> {
                val eventData = result.data.take(5)
                binding.tvNoEvent.visibility =
                    if (result.data.isEmpty()) View.VISIBLE else View.GONE
                binding.progressBar.visibility = View.GONE
                adapter.submitList(eventData)
            }

            is Result.Error -> {
                binding.progressBar.visibility = View.GONE
                binding.tvNoEvent.visibility = View.GONE
            }
        }
    }

    private fun observeSearchFinishedEvents(query: String) {
        homeViewModel.searchFinishedEvents(query).observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> {
                    binding.progressBar2.visibility = View.VISIBLE
                    binding.tvNoEvent2.visibility = View.GONE
                }

                is Result.Success -> {
                    binding.progressBar2.visibility = View.GONE
                    val eventData = result.data.take(5)
                    if (eventData.isEmpty()) {
                        binding.tvNoEvent2.visibility = View.VISIBLE
                        binding.rvEvent.visibility = View.GONE
                    } else {
                        binding.tvNoEvent2.visibility = View.GONE
                        binding.rvEvent.visibility = View.VISIBLE
                        eventVerticalAdapter.submitList(eventData)
                    }
                }

                is Result.Error -> {
                    binding.progressBar2.visibility = View.GONE
                    binding.rvEvent.visibility = View.GONE
                    binding.tvNoEvent2.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun navigateToDetailEvent(event: EventEntity) {
        val intent = Intent(requireContext(), DetailActivity::class.java).apply {
            putExtra(DetailActivity.EXTRA_EVENT_ID, event.id.toString())
        }
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
