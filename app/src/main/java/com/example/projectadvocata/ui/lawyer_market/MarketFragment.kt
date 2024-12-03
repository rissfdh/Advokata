@file:Suppress("unused", "RedundantSuppression")

package com.example.projectadvocata.ui.lawyer_market

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projectadvocata.data.local.entity.EventEntity
import com.example.projectadvocata.data.repo.Result
import com.example.projectadvocata.databinding.FragmentMarketBinding
import com.example.projectadvocata.ViewModelFactory
import com.example.projectadvocata.ui.adapter.EventAdapter
import com.example.projectadvocata.ui.detail.DetailActivity

class MarketFragment : Fragment() {

    private var _binding: FragmentMarketBinding? = null
    private val binding get() = _binding!!
    private lateinit var eventAdapter: EventAdapter
    private val upcomingViewModel: MarketViewModel by viewModels { ViewModelFactory.getInstance(requireActivity()) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMarketBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeUpcomingEvents()
    }

    private fun setupRecyclerView() {
        eventAdapter = EventAdapter { navigateToDetailEvent(it) }
        binding.rvEvent.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = eventAdapter
        }
    }


    private fun observeUpcomingEvents() {
        upcomingViewModel.findFinishedEvent().observe(viewLifecycleOwner) { result ->
            handleResult(result)
        }
    }

    private fun observeSearchUpcomingEvents(query: String) {
        upcomingViewModel.searchUpcomingEvents(query).observe(viewLifecycleOwner) { result ->
            handleResult(result)
        }
    }

    private fun handleResult(result: Result<List<EventEntity>>) {
        when (result) {
            is Result.Loading -> showLoading(true)
            is Result.Success -> {
                showLoading(false)
                updateEventList(result.data)
            }
            is Result.Error -> showError()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.tvNoLawyer.visibility = if (isLoading) View.GONE else binding.tvNoLawyer.visibility
    }

    private fun updateEventList(eventData: List<EventEntity>) {
        if (eventData.isEmpty()) {
            binding.tvNoLawyer.visibility = View.VISIBLE
            binding.rvEvent.visibility = View.GONE
        } else {
            binding.tvNoLawyer.visibility = View.GONE
            binding.rvEvent.visibility = View.VISIBLE
            eventAdapter.submitList(eventData)
        }
    }

    private fun showError() {
        binding.progressBar.visibility = View.GONE
        binding.rvEvent.visibility = View.GONE
        binding.tvNoLawyer.visibility = View.VISIBLE
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
