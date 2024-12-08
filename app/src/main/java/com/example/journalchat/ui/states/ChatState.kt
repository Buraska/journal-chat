package com.example.journalchat.ui.states

import com.example.journalchat.ui.uiModels.ChatUi
import com.example.journalchat.ui.uiModels.MessageUi


data class ChatState(
    val chat: ChatUi,
    val messages: List<MessageUi> = listOf(),
    val input: String = ""
)