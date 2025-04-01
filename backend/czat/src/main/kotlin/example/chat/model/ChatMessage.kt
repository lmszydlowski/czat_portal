package com.example.chat.model

import java.time.Instant

data class ChatMessage(
    val id: String? = null,
    val author: String,
    val content: String,
    val timestamp: Instant = Instant.now(),
    val userId: Long?
)
