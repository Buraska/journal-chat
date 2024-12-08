package com.example.journalchat.ui.viewModels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.semantics.text
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

class ChatViewModel(savedStateHandle: SavedStateHandle, chatRepository: ChatRepository, messageRepository: MessageRepository) : ViewModel() {

    private val id: Long = checkNotNull(savedStateHandle["chatId"])


    init {
        Log.wtf("wtf", id.toString())
    }


    val chat = chatRepository.getStream(id)

    val chatState: StateFlow<ChatState> =
        messageRepository.getStream(id).filterNotNull()
            .map { ChatState(chat = chatRepository.getStream(id).filterNotNull().first().toChatUi()) }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000L), ChatState(ChatUi()))


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
    }
