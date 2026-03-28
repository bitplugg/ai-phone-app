package com.aiapp.data

data class ChatMessage(
    val id: String = System.currentTimeMillis().toString(),
    val text: String,
    val isUser: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)
