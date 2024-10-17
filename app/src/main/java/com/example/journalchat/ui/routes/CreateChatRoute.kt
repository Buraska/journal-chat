package com.example.journalchat.ui.routes

import android.util.Log
import androidx.compose.runtime.Composable
import com.example.journalchat.ui.ChatCreationScreen

@Composable
fun CreateChatRoute(
    navigateUp: () -> Unit,
){
    Log.i("CreateChatRoute", "CreateChatRoute")
    ChatCreationScreen(
        navigateUp = navigateUp,
    )}