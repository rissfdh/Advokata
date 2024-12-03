@file:Suppress("unused", "RedundantSuppression")

package com.example.projectadvocata.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectadvocata.data.local.entity.EventEntity
import com.example.projectadvocata.data.repo.EventRepository
import kotlinx.coroutines.launch

class DetailViewModel(private val repository: EventRepository) : ViewModel() {

    fun getDetailEvent(query: String) = repository.getDetailEvent(query)
    fun saveEvent(event: EventEntity) {
        viewModelScope.launch {
            repository.setBookmarkedEvent(event, true)
        }
    }

    fun getBookmarkedNews() = repository.getFavoriteEvents()
    fun deleteEvent(event: EventEntity) {
        viewModelScope.launch {
            repository.setBookmarkedEvent(event, false)
        }
    }


}