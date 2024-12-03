package com.example.projectadvocata.ui.lawyer_market

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.projectadvocata.data.repo.EventRepository

class MarketViewModel(private val eventRepository: EventRepository) : ViewModel() {

    fun findFinishedEvent() = eventRepository.getFinishedEvents()
    fun searchUpcomingEvents(query: String) = eventRepository.searchEvents(query, isFinished = false)
}
