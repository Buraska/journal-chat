package com.example.journalchat.ui.viewModels

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.journalchat.data.repositories.ChatRepository
import com.example.journalchat.ui.states.ChatEditionState
import com.example.journalchat.ui.uiModels.toChat
import com.example.journalchat.ui.uiModels.toChatUi
import com.example.journalchat.ui.validatiors.ChatCreationValidator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ChatEditionViewModel(savedStateHandle: SavedStateHandle, private val chatRepository: ChatRepository): ViewModel() {

    private val id: Long = checkNotNull(savedStateHandle["chatId"])

    private val _chatState = MutableStateFlow(ChatEditionState())
    val chatState = _chatState.asStateFlow()
    init {
        viewModelScope.launch {
            chatRepository.getStream(id).filterNotNull()
                .map {ChatEditionState(nameInput = it.name, chatUi = it.toChatUi()) }
                .collect { _chatState.value = it }
        }
    }


    fun nameInputChanged(input: String){
        _chatState.update {  it.copy(nameInput = input)}
    }
    suspend fun editChat(): Boolean{
        val name = chatState.value.nameInput.trim()

        val error = ChatCreationValidator().validateName(name).errorMessage;
        _chatState.update{it.copy(nameError = error)}
        if (chatState.value.nameError != null){
            return false;
        }

        Log.i("Custom", "Updating item ${chatState.value.chatUi.toChat()}")
        chatRepository.updateItem(chatState.value.chatUi.toChat().copy(name = name))
        return true
    }

}