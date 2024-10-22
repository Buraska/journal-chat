package com.example.journalchat.repositories

import com.example.journalchat.data.Data
import com.example.journalchat.models.Chat


open class ChatRepo {

    fun getItems(): List<Chat>
    {
        return Data.chatList
    }

    fun getItem(id: String): Chat?
    {
        val result = Data.chatList.filter { it.name == id }
        return result.firstOrNull()
    }

    fun addItem(item: Chat)
    {
        Data.chatList.add(item)
    }

    fun removeItem(item: Chat)
    {
        Data.chatList.remove(item)
    }
}