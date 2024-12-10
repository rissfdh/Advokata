package com.example.projectadvocata.data.pref

data class UserModel(
    val token: String,
    val email: String,
    val name: String,
    val isLoggedIn: Boolean
)
