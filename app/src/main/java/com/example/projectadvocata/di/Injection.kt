package com.example.projectadvocata.di

import android.content.Context
import com.example.projectadvocata.data.repo.UserRepository
import com.example.projectadvocata.data.pref.UserPreference
import com.example.projectadvocata.data.pref.dataStore
import com.example.projectadvocata.data.retrofit.ApiConfig

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking


object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getUser().first() }
        val apiService = ApiConfig.getApiService(user.token)
        return UserRepository.getInstance(pref, apiService)
    }
}