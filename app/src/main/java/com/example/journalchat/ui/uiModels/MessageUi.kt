package com.example.journalchat.ui.uiModels

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


data class MessageUi (

    val id: Int?,
    val content:String,
    val isPrimary: Boolean = true,
    val date: LocalDateTime = LocalDateTime.now()
)
