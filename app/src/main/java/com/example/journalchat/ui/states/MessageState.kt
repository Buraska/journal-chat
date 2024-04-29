package com.example.journalchat.ui.states

import java.sql.Timestamp

data class MessageState (
    val content:String,
    val isPrimary: Boolean,
    val timestamp: Timestamp
) {
}