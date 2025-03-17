package com.example.journalchat.ui.uiModels

import androidx.compose.runtime.mutableStateOf
import com.example.journalchat.data.models.Chat
import com.example.journalchat.data.models.Tag
import java.time.LocalDateTime

data class TagUi(

    val id: Long = 0,
    val emojiCode: String,
    val lastAccess: LocalDateTime = LocalDateTime.now(),
    val name: String? = null,
)


fun Tag.toTagUi(): TagUi {
    val tagUi = TagUi(
        id = id,
        name = name,
        lastAccess = lastAccess,
        emojiCode = emojiCode
    )
    return tagUi
}

fun TagUi.toTag(): Tag {
    val tag = Tag(
        id = id,
        name = name,
        lastAccess = lastAccess,
        emojiCode = emojiCode
    )
    return tag
}

