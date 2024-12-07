package com.example.journalchat.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.journalchat.data.repositories.ChatRepository
import com.example.journalchat.ui.states.ChatListState
import com.example.journalchat.ui.uiModels.ChatUi
import com.example.journalchat.ui.uiModels.toChatUi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ChatListViewModel(chatRepository: ChatRepository) : ViewModel() {



    private val _chatListState = MutableStateFlow(ChatListState(mutableListOf<ChatUi>()))
    val chatListState: StateFlow<ChatListState> = _chatListState.asStateFlow()

    init {
        viewModelScope.launch { _chatListState.update { ChatListState(chatRepository.getAllStream().first().map { it.toChatUi()}) } }
    }


    fun createChat(chat: ChatUi){
        if (chat.name == ""){
            return
        }
        _chatListState.update { currentState -> currentState.copy(chats = currentState.chats.apply {  })}
    }
//https://chatgpt.com/c/167aeeea-2124-4990-98fc-baf750fb68c2
}