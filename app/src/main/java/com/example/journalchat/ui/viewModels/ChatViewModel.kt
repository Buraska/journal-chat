package com.example.journalchat.ui.viewModels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.journalchat.data.Data
import com.example.journalchat.ui.events.ChatEvent
import com.example.journalchat.ui.states.ChatListState
import com.example.journalchat.ui.states.ChatState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ChatViewModel : ViewModel() {
    private val _chatState = MutableStateFlow(ChatListState(Data.chatList))
    var chatState: StateFlow<ChatListState> = _chatState.asStateFlow()
    fun onEvent(event: ChatEvent){
        chatState = when(event){
            is ChatEvent.InputChanged -> {
                _chatState.update {currentState -> currentState.copy(in = event.text)}
            }

            is ChatEvent.SendMessage -> {
                chatState.copy(messages = chatState.messages.plus(event.message))
            }
            is ChatEvent.LoadState -> { //TODO Возможно есть какой то более правильный метод загрузки стэйта
                chatState.copy(event.state.name, event.state.messages)
            }
        }
    }
}