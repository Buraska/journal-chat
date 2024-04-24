package com.example.journalchat.ui.routes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.journalchat.ChatViewModel
import com.example.journalchat.ui.ChatListScreen

@Composable

fun ChatListRoute(
    viewModel: ChatViewModel = viewModel(),
    topBarNavIcon: @Composable () -> Unit,
    onCreateButton: () -> Unit
){
    val appState by viewModel.appState.collectAsState()

    ChatListScreen(
        chatList = appState.chats,
        topBarNavIcon = topBarNavIcon,
        onButtonClick = onCreateButton)
}