package com.example.projectadvocata.data.pref

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")

class UserPreference private constructor(private val dataStore: DataStore<Preferences>) {

    private val TOKEN_KEY = stringPreferencesKey("token")
    private val NAME_KEY = stringPreferencesKey("name")
    private val EMAIL_KEY = stringPreferencesKey("email")
    private val IS_LOGIN_KEY = booleanPreferencesKey("isLogin")

    suspend fun saveSession(user: UserModel) {
        try {
            dataStore.edit { preferences ->
                preferences[TOKEN_KEY] = user.token

                preferences[NAME_KEY] = user.name

                preferences[EMAIL_KEY] = user.email
                preferences[IS_LOGIN_KEY] = true
                Log.d("UserPreference", "Session saved: $user")
            }
        } catch (e: Exception) {
            Log.e("UserPreference", "Error saving session: ${e.message}")
        }
    }

    fun getSession(): Flow<UserModel> {
        return dataStore.data.map { preferences ->
            val token = preferences[TOKEN_KEY] ?: ""

            val name = preferences[NAME_KEY] ?: ""

            val email = preferences[EMAIL_KEY] ?: ""

            val isLoggedIn = preferences[IS_LOGIN_KEY] ?: false
            val user = UserModel(email, token, name, isLoggedIn)
            Log.d("UserPreference", "Retrieved session: $user")
            user
        }
    }

    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
        Log.d("UserPreference", "User logged out, session cleared.")
    }

    companion object {
        @Volatile
        private var INSTANCE: UserPreference? = null

        fun getInstance(dataStore: DataStore<Preferences>): UserPreference {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: UserPreference(dataStore).also { INSTANCE = it }
            }
        }
    }
}
