package com.example.journalchat.models

import android.graphics.drawable.Drawable

data class Chat (
    val name: String,
    val messages: List<Message>,
    val chatIcon: Drawable){

}