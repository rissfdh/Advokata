@file:Suppress("unused", "RedundantSuppression")

package com.example.projectadvocata.data.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.example.projectadvocata.data.local.entity.EventEntity
import com.example.projectadvocata.data.local.room.EventDao
import com.example.projectadvocata.data.remote.response.Event
import com.example.projectadvocata.data.remote.retrofit.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class EventRepository private constructor(
    private val apiService: ApiService,
    private val eventDao: EventDao
) {
    private val _showToastMessage = MutableLiveData<String>()
    val showToastMessage: LiveData<String> get() = _showToastMessage

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _detailEvent = MutableLiveData<Result<Event>>()

    fun getFinishedEvents(): LiveData<Result<List<EventEntity>>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getFinishedEvent()
            val listEvents = response.listEvents
            val eventList = listEvents.map { event ->
                val isBookmarked = eventDao.isEventBookmarked(event.name)
                EventEntity(
                    event.name,
                    event.beginTime,
                    event.imageLogo,
                    event.summary,
                    event.ownerName,
                    event.mediaCover,
                    event.registrants,
                    event.link,
                    event.description,
                    event.cityName,
                    event.quota,
                    event.id,
                    event.endTime,
                    event.category,
                    isBookmarked,
                    isActive = false
                )
            }

            eventDao.deleteFinishedEvents()
            eventDao.insertEvent(eventList)
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }

        val localData: LiveData<Result<List<EventEntity>>> =
            eventDao.getFinishedEvent().map { Result.Success(it) }
        emitSource(localData)
    }

    fun getUpcomingEvents(): LiveData<Result<List<EventEntity>>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getUpcomingEvent()
            val listEvents = response.listEvents
            val eventList = listEvents.map { event ->
                val isBookmarked = eventDao.isEventBookmarked(event.name)
                EventEntity(
                    event.name,
                    event.beginTime,
                    event.imageLogo,
                    event.summary,
                    event.ownerName,
                    event.mediaCover,
                    event.registrants,
                    event.link,
                    event.description,
                    event.cityName,
                    event.quota,
                    event.id,
                    event.endTime,
                    event.category,
                    isBookmarked,
                    isActive = true
                )
            }

            eventDao.deleteUpcomingEvents()
            eventDao.insertEvent(eventList)
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }

        val localData: LiveData<Result<List<EventEntity>>> =
            eventDao.getUpcomingEvent().map { Result.Success(it) }
        emitSource(localData)
    }

    suspend fun setBookmarkedEvent(event: EventEntity, bookmarkState: Boolean) {
        withContext(Dispatchers.IO) {
            event.isBookmarked = bookmarkState
            eventDao.updateEvent(event)
        }
    }

    fun getFavoriteEvents(): LiveData<Result<List<EventEntity>>> {
        val result = MediatorLiveData<Result<List<EventEntity>>>()
        result.value = Result.Loading
        val localData = eventDao.getBookmarkedEvent()
        result.addSource(localData) { events ->
            if (events.isNotEmpty()) {
                result.value = Result.Success(events)
            } else {
                result.value = Result.Error("No favorite events found")
            }
        }

        return result
    }

    fun searchEvents(query: String, isFinished: Boolean): LiveData<Result<List<EventEntity>>> {
        val result = MediatorLiveData<Result<List<EventEntity>>>()
        result.value = Result.Loading

        val searchResults =
            if (query.isEmpty()) {
                if (isFinished) eventDao.getFinishedEvent() else eventDao.getUpcomingEvent()
            } else {
                if (isFinished) eventDao.searchFinishedEvents("%$query%") else eventDao.searchUpcomingEvents(
                    "%$query%"
                )
            }

        result.addSource(searchResults) { events ->
            result.value =
                if (events.isNotEmpty()) Result.Success(events) else Result.Error("No events found")
        }

        return result
    }

    @Suppress("KotlinDeprecation")
    fun getDetailEvent(eventId: String): LiveData<Result<EventEntity>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getDetailEvent(eventId)
            val eventDetail = response.event
            eventDetail?.let { event ->
                val isBookmarked = eventDao.isEventBookmarked(event.name)
                val eventEntity = EventEntity(
                    event.name,
                    event.beginTime,
                    event.imageLogo,
                    event.summary,
                    event.ownerName,
                    event.mediaCover,
                    event.registrants,
                    event.link,
                    event.description,
                    event.cityName,
                    event.quota,
                    event.id,
                    event.endTime,
                    event.category,
                    isBookmarked,
                    isActive = true
                )
                eventDao.insertEvent(listOf(eventEntity))
            }
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
        val localData = eventDao.getEventById(eventId).map { eventEntity ->
            Result.Success(eventEntity) as Result<EventEntity>
        }
        emitSource(localData)
    }

    companion object {
        @Volatile
        private var instance: EventRepository? = null
        fun getInstance(
            apiService: ApiService,
            eventDao: EventDao
        ): EventRepository =
            instance ?: synchronized(this) {
                instance ?: EventRepository(apiService, eventDao)
            }.also { instance = it }
    }
}