package com.example.journalchat.data

import com.example.journalchat.models.Chat

data class AppState(
    val chats: MutableList<Chat>
)