package com.example.journalchat.ui.states

import com.example.journalchat.ui.uiModels.ChatUi

data class ChatEditionState (
    val nameInput: String = "",
    val chatUi: ChatUi = ChatUi(),
    val nameError: String? = null
    )