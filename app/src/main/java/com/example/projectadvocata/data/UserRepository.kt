package com.example.projectadvocata.data

import android.util.Log
import com.example.projectadvocata.data.pref.UserModel
import com.example.projectadvocata.data.pref.UserPreference
import com.example.projectadvocata.data.response.LoginResult
import com.example.projectadvocata.data.retrofit.ApiService
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException

class UserRepository private constructor(
    private val userPreference: UserPreference,
    private val apiService: ApiService
) {
    suspend fun register(name: String, email: String, password: String): Result<Unit> {
        return try {
            val response = apiService.register(name, email, password)
            if (!response.error) {
                Result.success(Unit)
            } else {
                Result.failure(Exception(response.message))
            }
        } catch (e: HttpException) {
            when (e.code()) {
                400 -> Result.failure(Exception("Email sudah digunakan atau format email salah"))
                else -> Result.failure(e)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun login(email: String, password: String): Result<LoginResult> {
        return try {
            val response = apiService.login(email, password)
            if (!response.error) {
                Result.success(response.loginResult)
            } else {
                Result.failure(Exception(response.message))
            }
        } catch (e: HttpException) {
            when (e.code()) {
                401 -> Result.failure(Exception("Email atau password salah"))
                400 -> Result.failure(Exception("Lengkapi data terlebih dahulu"))
                else -> Result.failure(e)
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