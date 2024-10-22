package com.example.journalchat.ui.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.journalchat.ui.states.ChatListState
import com.example.journalchat.data.Data
import com.example.journalchat.models.Chat
import com.example.journalchat.repositories.ChatRepo
import com.example.journalchat.ui.events.CreateChatEvent
import com.example.journalchat.ui.states.ChatCreationState
import com.example.journalchat.ui.validatiors.ChatCreationValidator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CreateChatViewModel : ViewModel() {

    private val chatRepository = ChatRepo()
    private val _chatListState = MutableStateFlow(ChatListState(Data.chatList))
    val chatListState: StateFlow<ChatListState> = _chatListState.asStateFlow()

    var chatState by mutableStateOf(ChatCreationState())


    fun onEvent(event:CreateChatEvent): Boolean{
        when (event){
            is CreateChatEvent.NameChanged ->{
                chatState = chatState.copy(name = event.name)
                return true
            }
            is CreateChatEvent.Submit -> {
                val name = chatState.name.trim()

                ChatCreationValidator().validateName(name)
                val error = ChatCreationValidator().validateName(name).errorMessage;
                chatState = chatState.copy(nameError = error)
                if (chatState.nameError != null){
                    return false;
                }

                val chat = Chat(name, mutableListOf(), null);
                _chatListState.update { currentState -> currentState.copy(chats = currentState.chats.apply { add(chat) })}
                return true
            }
        }
    }

}