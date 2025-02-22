package com.example.journalchat.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.journalchat.data.repositories.ChatRepository
import com.example.journalchat.ui.states.ChatListState
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



    val chatListState: StateFlow<ChatListState> =
        chatRepository.getAllStream().map { ChatListState(it.map { it.toChatUi() }) }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000L), ChatListState(listOf()))
}