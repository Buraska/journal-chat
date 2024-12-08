package com.example.journalchat.ui.routes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.journalchat.ui.AppViewModelProvider
import com.example.journalchat.ui.viewModels.ChatListViewModel
import com.example.journalchat.ui.ChatListScreen

@Composable
fun ChatListRoute(
    exposeDrawer: () -> Unit,
    onCreateButton: () -> Unit,
    onItemClicked: (Long) -> Unit,
    viewModel: ChatListViewModel = viewModel(factory = AppViewModelProvider.Factory)
){
    val appState by viewModel.chatListState.collectAsState()

    ChatListScreen(
        chatList = appState.chats,
        exposeDrawer = exposeDrawer,
        onItemClicked = onItemClicked,
        onButtonClick = onCreateButton)
}