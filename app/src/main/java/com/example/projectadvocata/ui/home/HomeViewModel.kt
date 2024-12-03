package com.example.projectadvocata.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.projectadvocata.data.repo.EventRepository

class HomeViewModel(private val repository: EventRepository) : ViewModel() {

    fun getFinishedEvents() = repository.getFinishedEvents()
    fun searchFinishedEvents(query: String) = repository.searchEvents(query, isFinished = true)
}
