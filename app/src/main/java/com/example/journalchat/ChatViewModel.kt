package com.example.journalchat

import androidx.lifecycle.ViewModel
import com.example.journalchat.data.AppState
import com.example.journalchat.data.Data
import com.example.journalchat.models.Chat
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ChatViewModel : ViewModel() {

    private val _appState = MutableStateFlow(AppState(Data.chatList))
    val appState: StateFlow<AppState> = _appState.asStateFlow()

    fun createChat(chat: Chat){
        if (chat.name == ""){
            return
        }
        _appState.update {currentState -> currentState.copy(chats = currentState.chats.apply { add(chat) })}
    }

}