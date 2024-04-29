package com.example.journalchat.ui.routes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.journalchat.ChatViewModel
import com.example.journalchat.ui.ChatListScreen

@Composable
fun ChatListRoute(
    exposeDrawer: () -> Unit,
    onCreateButton: () -> Unit,
    onItemClicked: (String) -> Unit,
    viewModel: ChatViewModel = viewModel()
){
    val appState by viewModel.chatListState.collectAsState()

    ChatListScreen(
        chatList = appState.chats,
        exposeDrawer = exposeDrawer,
        onItemClicked = onItemClicked,
        onButtonClick = onCreateButton)
}