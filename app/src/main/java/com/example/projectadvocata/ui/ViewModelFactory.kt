package com.example.projectadvocata.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.projectadvocata.data.repo.UserRepository
import com.example.projectadvocata.di.Injection
import com.example.projectadvocata.ui.login.LoginViewModel
import com.example.projectadvocata.ui.profile.ProfileViewModel
import com.example.projectadvocata.ui.register.RegisterViewModel
class ViewModelFactory(private val repository: UserRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(repository) as T
            }
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(repository) as T
            }
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                ProfileViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }

    companion object {
        fun getInstance(context: Context): ViewModelFactory {
            val repository = Injection.provideRepository(context)
            return ViewModelFactory(repository)
        }
        fun getAuthInstance(context: Context) = ViewModelFactory(Injection.provideRepository(context))

    }

}

