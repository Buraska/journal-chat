package com.example.journalchat.ui.states

import androidx.compose.ui.text.input.TextFieldValue
import com.example.journalchat.ui.uiModels.ChatUi
import com.example.journalchat.ui.uiModels.MessageUi


data class ChatState(
    val chat: ChatUi = ChatUi(),
    val messages: List<MessageUi> = listOf(),
    val selectedMessages: List<MessageUi> = listOf(),
    val mode: ChatMode = ChatMode.Chatting,
    val input: TextFieldValue = TextFieldValue("")
)

enum class ChatMode {
    Chatting,
    Editing,
    Selecting,
    Replying
}