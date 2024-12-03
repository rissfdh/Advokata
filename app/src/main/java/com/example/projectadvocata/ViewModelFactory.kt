@file:Suppress("unused", "RedundantSuppression")

package com.example.projectadvocata

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.projectadvocata.di.Injection
import com.example.projectadvocata.ui.detail.DetailViewModel
import com.example.projectadvocata.ui.home.HomeViewModel
import com.example.projectadvocata.ui.lawyer_market.MarketViewModel
import kotlin.also
import kotlin.jvm.java

class ViewModelFactory private constructor(
    private val eventRepository: com.example.projectadvocata.data.repo.EventRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MarketViewModel::class.java) -> {
                MarketViewModel(eventRepository) as T
            }
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(eventRepository) as T
            }
            modelClass.isAssignableFrom(DetailViewModel::class.java) -> {
                DetailViewModel(eventRepository) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(
                    com.example.projectadvocata.di.Injection.provideEventRepository(context)
                ).also { instance = it }
            }
    }
}
