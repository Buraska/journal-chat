package com.example.journalchat.ui.events

import com.example.journalchat.ui.uiModels.ChatUi
import com.example.journalchat.ui.uiModels.MessageUi


sealed class ChatEvent {
    data class InputChanged(val text: String): ChatEvent()
    data class SendMessage(val message: MessageUi): ChatEvent()
    data class LoadState(val state: ChatUi): ChatEvent()
}