package com.example.projectadvocata.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.projectadvocata.data.pref.UserModel
import com.example.projectadvocata.data.repo.UserRepository
import kotlinx.coroutines.launch

class ProfileViewModel(private val logoutRepository: UserRepository) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isLoggedOut = MutableLiveData<Boolean>()
    val isLoggedOut: LiveData<Boolean> = _isLoggedOut

    fun getSession(): LiveData<UserModel> {
        return logoutRepository.getSession().asLiveData()
    }
    fun logout() {
        viewModelScope.launch {
            logoutRepository.logout()
            _isLoggedOut.value = true
        }
    }
}