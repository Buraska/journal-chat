package com.example.journalchat.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chat")
data class Chat (

    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val name: String,
    val chatIcon: Int?)