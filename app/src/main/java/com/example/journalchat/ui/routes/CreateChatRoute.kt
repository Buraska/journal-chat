package com.example.journalchat.ui.routes

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.journalchat.ChatViewModel
import com.example.journalchat.ui.CreateChatScreen

@Composable
fun CreateChatRoute(
    topBarNavIcon: @Composable () -> Unit,
){
    CreateChatScreen(
        topBarNavIcon = topBarNavIcon,
    )}