package com.example.journalchat.models

import android.graphics.drawable.Drawable

data class Chat (
    val name: String,
    val messages: MutableList<Message>,
    val chatIcon: Drawable?){

}