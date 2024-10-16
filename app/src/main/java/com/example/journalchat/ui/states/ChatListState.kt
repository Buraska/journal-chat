package com.example.journalchat.ui.states

import com.example.journalchat.models.Chat

data class ChatListState(
    val chats: MutableList<Chat>,
)