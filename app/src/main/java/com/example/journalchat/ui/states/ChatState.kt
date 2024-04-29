package com.example.journalchat.ui.states

import com.example.journalchat.models.Message

data class ChatState(
    val name: String = "",
    val messages: List<Message> = listOf(),
    val input: String = ""
)