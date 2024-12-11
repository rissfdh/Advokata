package com.example.projectadvocata.ui.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectadvocata.data.repo.UserRepository
import com.example.projectadvocata.data.pref.UserModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
class LoginViewModel(private val loginRepository: UserRepository) : ViewModel() {

    private val _isLoginSuccessful = MutableLiveData<Boolean>()
    val isLoginSuccessful: LiveData<Boolean> = _isLoginSuccessful

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getSession(): Flow<UserModel> {
        return loginRepository.getUser()
    }

    fun login(email: String, password: String) {
        _isLoading.value = true
        viewModelScope.launch {
            val result = loginRepository.login(email, password)
            result.onSuccess { loginResult ->
                val user = UserModel(email, loginResult.token, loginResult.name, true)
                loginRepository.saveSession(user)  // Menyimpan sesi pengguna
                _isLoginSuccessful.value = true
                _isLoading.value = false
                _errorMessage.value = null
            }.onFailure { exception ->
                _isLoginSuccessful.value = false
                _isLoading.value = false
                _errorMessage.value = exception.message
            }
        }
    }
}
