package com.example.journalchat.ui.viewModels

import androidx.lifecycle.ViewModel
import com.example.journalchat.ui.states.ChatListState
import com.example.journalchat.data.Data
import com.example.journalchat.models.Chat
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ChatListViewModel : ViewModel() {

    private val _chatListState = MutableStateFlow(ChatListState(Data.chatList))
    val chatListState: StateFlow<ChatListState> = _chatListState.asStateFlow()

    fun createChat(chat: Chat){
        if (chat.name == ""){
            return
        }
        _chatListState.update { currentState -> currentState.copy(chats = currentState.chats.apply { add(chat) })}
    }
//https://chatgpt.com/c/167aeeea-2124-4990-98fc-baf750fb68c2
}