package com.example.journalchat.ui.uiModels

import com.example.journalchat.data.models.Message
import com.example.journalchat.data.models.Tag
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


data class MessageUi (

    val id: Long = 0,
    val chatId: Long,
    val tagId: Long? = null,
    val tag: TagUi? = null,
    val referenceId: Long? = null,
    val reference: MessageUi? = null,
    val content:String,
    val isPrimary: Boolean = true,
    val date: LocalDateTime = LocalDateTime.now(),
    var isSelected: Boolean = false
)

fun MessageUi.toMessage(): Message {
    return Message(id, chatId, tagId, referenceId, content, isPrimary, date)
}

fun Message.toMessageUi(): MessageUi {
    return MessageUi(id, chatId, tagId,null, referenceId,null, content, isPrimary, date)
}

fun Message.toMessageUi(reference: MessageUi?, tag: TagUi?): MessageUi {
    return MessageUi(id, chatId, tag?.id ,tag, referenceId, reference, content, isPrimary, date)
}
