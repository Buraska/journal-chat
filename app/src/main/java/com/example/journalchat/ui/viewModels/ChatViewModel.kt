package com.example.journalchat.ui.viewModels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.journalchat.ui.events.ChatEvent
import com.example.journalchat.ui.states.ChatState

class ChatViewModel : ViewModel() {

    var chatState by mutableStateOf(ChatState())

    fun onEvent(event: ChatEvent){
        when(event){
            is ChatEvent.InputChanged -> {
                chatState = chatState.copy(input = event.text)
            }
        }
    }
}