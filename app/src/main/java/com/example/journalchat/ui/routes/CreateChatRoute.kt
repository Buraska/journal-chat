package com.example.journalchat.ui.routes

import androidx.compose.runtime.Composable
import com.example.journalchat.ui.ChatCreationScreen

@Composable
fun CreateChatRoute(
    navigateUp: () -> Unit,
){
    ChatCreationScreen(
        navigateUp = navigateUp,
    )}