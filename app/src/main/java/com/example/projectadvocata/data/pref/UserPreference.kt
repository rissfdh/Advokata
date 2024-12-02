package com.example.projectadvocata.data.pref

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")

class UserPreference private constructor(private val dataStore: DataStore<Preferences>) {


    suspend fun saveSession(user: UserModel) {
        try {
            dataStore.edit { preferences ->
                preferences[EMAIL_KEY] = user.email
                preferences[TOKEN_KEY] = user.token
                preferences[IS_LOGIN_KEY] = true
                Log.d("UserPreference", "Session saved: $user")
            }
        } catch (e: Exception) {
            Log.e("UserPreference", "Error saving session: ${e.message}")
        }
    }

    fun getSession(): Flow<UserModel> {
        return dataStore.data.map { preferences ->
            val email = preferences[EMAIL_KEY] ?: ""
            val token = preferences[TOKEN_KEY] ?: ""
            val isLoggedIn = preferences[IS_LOGIN_KEY] ?: false
            val user = UserModel(email, token, isLoggedIn)
            Log.d("UserPreference", "Retrieved session: $user")
            user
        }
    }


    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    fun getUser(): Flow<UserModel> {
        return dataStore.data.map { preferences ->
            UserModel(
                email = preferences[EMAIL_KEY] ?: "",
                token = preferences[TOKEN_KEY] ?: "",
                isLoggedIn = preferences[IS_LOGIN_KEY] ?: false
            )
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserPreference? = null

        private val EMAIL_KEY = stringPreferencesKey("email")
        private val TOKEN_KEY = stringPreferencesKey("token")
        private val IS_LOGIN_KEY = booleanPreferencesKey("isLogin")

        fun getInstance(dataStore: DataStore<Preferences>): UserPreference {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: UserPreference(dataStore).also { INSTANCE = it }
            }
        }
    }
}