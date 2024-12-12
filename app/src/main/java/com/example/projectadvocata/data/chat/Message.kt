package com.example.projectadvocata.data.chat

data class Message(
    val text: String,
    val isUser: Boolean // true = user, false = bot
)
