package com.example.projectadvocata.data.pref

data class UserModel(
    val email: String,
    val token: String,
    val isLoggedIn: Boolean = false
)