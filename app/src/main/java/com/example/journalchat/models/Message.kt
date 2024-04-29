package com.example.journalchat.models

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class Message (
    val content:String,
    val isPrimary: Boolean = true,
    val dateTime: LocalDateTime = LocalDateTime.parse("2010-04-24 12:24:35", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
)