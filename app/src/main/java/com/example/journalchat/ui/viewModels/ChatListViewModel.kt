package com.example.journalchat.ui.viewModels

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.journalchat.data.repositories.ChatRepository
import com.example.journalchat.ui.states.ChatListMode
import com.example.journalchat.ui.states.ChatListState
import com.example.journalchat.ui.states.ChatMode
import com.example.journalchat.ui.uiModels.ChatUi
import com.example.journalchat.ui.uiModels.toChatUi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ChatListViewModel(chatRepository: ChatRepository) : ViewModel() {


    private val _chatListState = MutableStateFlow(ChatListState(listOf()))
    val chatListState: StateFlow<ChatListState> = _chatListState.asStateFlow()

    init {
        viewModelScope.launch {
            chatRepository.getAllStream().map { ChatListState(it.map { it.toChatUi() }) }
                .collect { _chatListState.value = it }
        }
    }

    fun getSelectedCount(): Int{
        return chatListState.value.selectedChats.count()
    }
    fun clearSelection() {
        _chatListState.value.selectedChats.map { it.isSelected = false }
        _chatListState.update { currentState ->
            currentState.copy(selectedChats = listOf(), chatListMode = ChatListMode.Default)
        }
    }
    fun selectChat(chatUi: ChatUi): Unit {
        chatUi.isSelected = !chatUi.isSelected
        if (chatUi.isSelected) {
            _chatListState.update {
                it.copy(
                    selectedChats = it.selectedChats.plus(chatUi),
                    chatListMode = ChatListMode.Selecting
                )
            }
        } else {
            _chatListState.update {
                it.copy(
                    selectedChats = it.selectedChats.minus(chatUi),
                )
            }
            if (_chatListState.value.selectedChats.isEmpty()) {
                _chatListState.update {
                    it.copy(
                        chatListMode = ChatListMode.Default,
                    )
                }
            }
        }
    }

    fun selectChat(chatUi: Long) {
        chatListState.value.chats.find { it.id == chatUi }?.let { selectChat(it) } ?: { Log.e("Null error", "Can`t find chat to select")}
    }

}