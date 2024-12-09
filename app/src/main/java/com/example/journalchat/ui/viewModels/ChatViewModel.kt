package com.example.journalchat.ui.viewModels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.semantics.text
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.journalchat.data.models.Message
import com.example.journalchat.data.repositories.ChatRepository
import com.example.journalchat.data.repositories.MessageRepository
import com.example.journalchat.ui.events.ChatEvent
import com.example.journalchat.ui.states.ChatCreationState
import com.example.journalchat.ui.states.ChatListState
import com.example.journalchat.ui.states.ChatState
import com.example.journalchat.ui.uiModels.ChatUi
import com.example.journalchat.ui.uiModels.toChatUi
import com.example.journalchat.ui.uiModels.toMessageUi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class ChatViewModel(
    savedStateHandle: SavedStateHandle,
    private val chatRepository: ChatRepository,
    private val messageRepository: MessageRepository
) : ViewModel() {

    private val id: Long = checkNotNull(savedStateHandle["chatId"])

    val chat = chatRepository.getStream(id)

    private val _chatState = MutableStateFlow(ChatState(ChatUi()))
    val chatState: StateFlow<ChatState> = _chatState.asStateFlow()

    init {
        viewModelScope.launch {
            messageRepository.getAllStream(id).filterNotNull()
                .map { messages ->
                    ChatState(
                        chat = chatRepository.getStream(id).filterNotNull().first().toChatUi(),
                        messages = messages.map { it.toMessageUi() }
                    )
                }
                .collect { _chatState.value = it }
        }
    }

    fun sendMessage() {
        if (chatState.value.input == "") return
        viewModelScope.launch {
            messageRepository.insertItem(Message(0, id, chatState.value.input, true, LocalDateTime.now()))
            inputChanged("")
        }
    }

    fun inputChanged(input: String) {
        _chatState.update { currentState -> currentState.copy(input = input) }
    }
}




fun onEvent(event: ChatEvent) {
    when (event) {
        is ChatEvent.InputChanged -> {
        }

        is ChatEvent.SendMessage -> {
        }

        is ChatEvent.LoadState -> {

        }
    }
}

