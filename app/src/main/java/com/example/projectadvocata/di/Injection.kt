@file:Suppress("unused", "RedundantSuppression")

package com.example.projectadvocata.di

import android.content.Context
import com.example.projectadvocata.data.local.room.EventDatabase
import com.example.projectadvocata.data.remote.retrofit.ApiConfig
import com.example.projectadvocata.data.repo.EventRepository

object Injection {
    fun provideEventRepository(context: Context): EventRepository {
        val apiService = ApiConfig.getApiService()
        val database = EventDatabase.getInstance(context)
        val dao = database.eventDao()
        return EventRepository.getInstance(apiService, dao)
    }
}