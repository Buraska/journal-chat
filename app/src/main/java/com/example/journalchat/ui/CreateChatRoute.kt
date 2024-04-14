package com.example.journalchat.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.journalchat.ChatViewModel

@Composable
fun CreateChatRoute(
    viewModel: ChatViewModel = viewModel(),
    topBarNavIcon: @Composable () -> Unit,
){
    CreateChatScreen(
        topBarNavIcon = topBarNavIcon,
        onButtonClick = {   chat -> viewModel.createChat(chat)  })
}