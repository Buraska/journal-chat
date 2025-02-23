package com.example.journalchat.ui.uiModels

import androidx.compose.runtime.mutableStateOf
import com.example.journalchat.data.models.Chat

data class ChatUi(

    val id: Long = 0,
    val name: String = "",
    val messages: List<MessageUi> = listOf(), // TODO do we need messages here?
    val chatIcon: Int? = null,
    var isSelected: Boolean = false
)


fun Chat.toChatUi(): ChatUi {
    val chatUi = ChatUi(
        id = id,
        name = name,
    )
    return chatUi
}

fun ChatUi.toChat(): Chat {
    val chat = Chat(
        id = id,
        name = name,
        chatIcon = chatIcon
    )
    return chat
}

