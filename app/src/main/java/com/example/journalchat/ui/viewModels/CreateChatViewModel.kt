package com.example.journalchat.ui.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.journalchat.data.AppState
import com.example.journalchat.data.Data
import com.example.journalchat.models.Chat
import com.example.journalchat.models.Message
import com.example.journalchat.ui.events.CreateChatEvent
import com.example.journalchat.ui.states.CreateChatState
import com.example.journalchat.ui.validatiors.CreateChatValidator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CreateChatViewModel : ViewModel() {

    private val _appState = MutableStateFlow(AppState(Data.chatList))
    val appState: StateFlow<AppState> = _appState.asStateFlow()

    var chatState by mutableStateOf(CreateChatState())


    fun onEvent(event:CreateChatEvent){
        when (event){
            is CreateChatEvent.NameChanged ->{
                chatState = chatState.copy(name = event.name)
            }
            is CreateChatEvent.Submit -> {
                CreateChatValidator().validateName(chatState.name)
                val error = CreateChatValidator().validateName(chatState.name).errorMessage;
                chatState = chatState.copy(nameError = error)

                val chat = Chat(chatState.name, mutableListOf(), null);
                _appState.update {currentState -> currentState.copy(chats = currentState.chats.apply { add(chat) })}



            }
        }
    }

}