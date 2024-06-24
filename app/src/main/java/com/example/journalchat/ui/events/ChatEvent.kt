package com.example.journalchat.ui.events

import com.example.journalchat.models.Chat
import com.example.journalchat.models.Message
import com.example.journalchat.ui.states.ChatState

sealed class ChatEvent {
    data class InputChanged(val text: String): ChatEvent()
    data class SendMessage(val message: Message): ChatEvent()
    data class LoadState(val state: Chat): ChatEvent()
}