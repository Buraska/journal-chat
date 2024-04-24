package com.example.journalchat.ui.events

sealed class CreateChatEvent {
    data class NameChanged(val name: String): CreateChatEvent()
    data object Submit : CreateChatEvent()
}