package com.example.journalchat.ui.viewModels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.semantics.text
import androidx.lifecycle.ViewModel
import com.example.journalchat.ui.events.ChatEvent
import com.example.journalchat.ui.states.ChatListState
import com.example.journalchat.ui.states.ChatState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ChatViewModel : ViewModel() {
    private val _chatState = MutableStateFlow(ChatState())
    var chatState: StateFlow<ChatState> = _chatState.asStateFlow()


    fun onEvent(event: ChatEvent) {
        when (event) {
            is ChatEvent.InputChanged -> {
                _chatState.update { currentState ->
                    currentState.copy(input = event.text) }
            }
            is ChatEvent.SendMessage -> {
                _chatState.update { currentState ->
                    currentState.copy(messages = currentState.messages + event.message)
                }
            }
            is ChatEvent.LoadState -> {
                _chatState.update { currentState ->
                    currentState.copy(name = event.state.name, messages = event.state.messages)
                }
            }
        }
    }
    }
