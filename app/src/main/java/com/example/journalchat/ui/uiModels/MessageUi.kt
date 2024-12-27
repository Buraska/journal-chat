package com.example.journalchat.ui.uiModels

import com.example.journalchat.data.models.Message
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


data class MessageUi (

    val id: Long = 0,
    val chatId: Long,
    val referenceId: Long? = null,
    val reference: MessageUi? = null,
    val content:String,
    val isPrimary: Boolean = true,
    val date: LocalDateTime = LocalDateTime.now(),
    var isSelected: Boolean = false
)

fun MessageUi.toMessage(): Message {
    return Message(id, chatId, referenceId, content, isPrimary, date)
}

fun Message.toMessageUi(): MessageUi {
    return MessageUi(id, chatId, referenceId, null, content, isPrimary, date)
}

