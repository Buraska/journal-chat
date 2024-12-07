package com.example.journalchat.ui.states

import com.example.journalchat.ui.uiModels.MessageUi


data class ChatState(
    val name: String = "",
    val messages: List<MessageUi> = listOf(),
    val input: String = ""
)