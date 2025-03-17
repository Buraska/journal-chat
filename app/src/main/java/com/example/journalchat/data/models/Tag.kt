package com.example.journalchat.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "tag")
data class Tag (
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val emojiCode: String,
    val lastAccess: LocalDateTime = LocalDateTime.now(),
    val name: String? = null,
)