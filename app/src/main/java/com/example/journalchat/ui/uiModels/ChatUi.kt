package com.example.journalchat.ui.uiModels

import com.example.journalchat.data.models.Chat

data class ChatUi(

    val id: Long = 0,
    val name: String = "",
    val messages: List<MessageUi> = listOf(),
    val chatIcon: Int? = null
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

