package com.example.journalchat.ui.states

import androidx.compose.ui.text.input.TextFieldValue
import com.example.journalchat.ui.uiModels.ChatUi
import com.example.journalchat.ui.uiModels.MessageUi
import com.example.journalchat.ui.uiModels.TagUi


data class ReplyingState(
    val replyingMessage: MessageUi,
    val userTags: List<TagUi> = listOf(),
    val filteringTag: TagUi? = null,
    val messages: List<MessageUi> = listOf(),
    val selectedMessages: List<MessageUi> = listOf(),
    val mode: ChatMode = ChatMode.Chatting,
    val input: TextFieldValue = TextFieldValue(""),
    val searchInput: TextFieldValue = TextFieldValue(""),
    val tags: List<TagUi> = listOf()
)
