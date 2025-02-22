package com.example.journalchat.ui.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.journalchat.data.repositories.ChatRepository
import com.example.journalchat.ui.states.ChatCreationState
import com.example.journalchat.ui.uiModels.ChatUi
import com.example.journalchat.ui.validatiors.ChatCreationValidator
import com.example.journalchat.ui.uiModels.toChat

class CreateChatViewModel(private val chatRepository: ChatRepository) : ViewModel() {
    var chatState by mutableStateOf(ChatCreationState())
    private set

    fun nameChanged(name: String){
        chatState = chatState.copy(name = name)
    }

    suspend fun createChat(): Boolean{
        val name = chatState.name.trim()

        val error = ChatCreationValidator().validateName(name).errorMessage;
        chatState = chatState.copy(nameError = error)
        if (chatState.nameError != null){
            return false;
        }

        val chat = ChatUi(0, name, mutableListOf(), null);
        chatRepository.insertItem(chat.toChat())
        return true
    }

}