package com.example.projectadvocata.ui.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectadvocata.data.UserRepository
import com.example.projectadvocata.data.pref.UserModel
import kotlinx.coroutines.launch

class LoginViewModel(private val loginRepository: UserRepository) : ViewModel() {

    private val _isLoginSuccessful = MutableLiveData<Boolean>()
    val isLoginSuccessful: LiveData<Boolean> = _isLoginSuccessful

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading


    private fun saveSession(user: UserModel) {
        viewModelScope.launch {
            try {
                Log.d("LoginViewModel", "Saving session for: $user")
                loginRepository.saveSession(user)
                Log.d("LoginViewModel", "Session saved successfully")
            } catch (e: Exception) {
                Log.e("LoginViewModel", "Error saving session: ${e.message}")
            }
        }
    }

    fun login(email: String, password: String) {
        _isLoading.value = true
        viewModelScope.launch {
            val result = loginRepository.login(email, password)
            result.onSuccess { loginResult ->
                Log.d("LoginViewModel", "Login successful: ${loginResult.token}")
                saveSession(UserModel(email, loginResult.token, true))
                _isLoginSuccessful.value = true
                _isLoading.value = false
                _errorMessage.value = null
            }.onFailure { exception ->
                Log.e("LoginViewModel", "Login failed: ${exception.message}")
                _isLoginSuccessful.value = false
                _isLoading.value = false
                _errorMessage.value = exception.message
            }
        }
    }

}
