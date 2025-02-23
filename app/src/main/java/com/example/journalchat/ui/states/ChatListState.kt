package com.example.journalchat.ui.states

import com.example.journalchat.ui.uiModels.ChatUi


data class ChatListState(
    val chats: List<ChatUi>,
    val selectedChats: List<ChatUi> = listOf(),
    val chatListMode: ChatListMode = ChatListMode.Default
)

enum class ChatListMode{
    Default,
    Selecting
}