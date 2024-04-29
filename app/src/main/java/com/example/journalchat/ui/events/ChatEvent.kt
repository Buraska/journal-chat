package com.example.journalchat.ui.events

sealed class ChatEvent {
    data class InputChanged(val text: String): ChatEvent()
}