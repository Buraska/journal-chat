package com.example.journalchat.ui.routes

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.journalchat.data.Data
import com.example.journalchat.models.Chat
import com.example.journalchat.ui.ChatScreen
import com.example.journalchat.ui.events.ChatEvent
import com.example.journalchat.ui.states.ChatState
import com.example.journalchat.ui.viewModels.ChatViewModel

@Composable
fun ChatRoute(navigateUp: () -> Unit, chatName: String?,
              viewModel: ChatViewModel = viewModel()
) {
    val chat = Data.chatList.find ({ it.name == chatName})
    if (chat == null){
        Log.e("CHAT ERROR", "Cannot find chat name ${chatName}")
    }
    else viewModel.onEvent(ChatEvent.LoadState(chat))
    ChatScreen(viewModel = viewModel, navigateUp = navigateUp)


}