package com.example.projectadvocata.data.repo

import android.util.Log
import com.example.projectadvocata.data.pref.UserModel
import com.example.projectadvocata.data.pref.UserPreference
import com.example.projectadvocata.data.response.LoginResult
import com.example.projectadvocata.data.retrofit.ApiService
import com.example.projectadvocata.data.retrofit.LoginRequest
import com.example.projectadvocata.data.retrofit.RegisterRequest
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException

class UserRepository private constructor(
    private val userPreference: UserPreference,
    private val apiService: ApiService
) {
    suspend fun register(name: String, email: String, password: String): Result<Unit> {
        return try {
            val response = apiService.register(RegisterRequest(name, email, password))
            if (!response.error) {
                Result.success(Unit)
            } else {
                Result.failure(Exception(response.message))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun login(email: String, password: String): Result<LoginResult> {
        return try {
            val response = apiService.login(LoginRequest(email, password))
            if (!response.error) {
                Result.success(response.loginResult)
            } else {
                Result.failure(Exception(response.message))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    suspend fun saveSession(user: UserModel) {
        Log.d("UserRepository", "Saving session for user: $user")
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null

        fun getInstance(userPreference: UserPreference, apiService: ApiService): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(userPreference, apiService)
            }.also { instance = it
            }
    }

}