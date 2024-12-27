package com.example.journalchat.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime
import java.util.Date


@Entity(tableName = "message")
data class Message (

    @PrimaryKey(autoGenerate = true)
    val id: Long,

    @ColumnInfo(name = "chat_id")
    val chatId: Long,

    val referenceId: Long?,

    val content:String,
    val isPrimary: Boolean = true,
    val date: LocalDateTime
)