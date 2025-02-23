package com.example.journalchat.data.repositories

import androidx.room.Query
import com.example.journalchat.data.daos.ChatDao
import com.example.journalchat.data.models.Chat
import com.example.journalchat.data.models.Message
import com.example.journalchat.ui.uiModels.ChatUi
import kotlinx.coroutines.flow.Flow


class ChatRepository(private val chatDao: ChatDao) {

    fun getAllStream(): Flow<List<Chat>> = chatDao.getAll()

    fun getStream(id: Long): Flow<Chat?> = chatDao.get(id)

    suspend fun insertItem(item: Chat) = chatDao.insert(item)

    suspend fun deleteItem(item: Chat) = chatDao.delete(item)

    suspend fun deleteChats(chats: List<Chat>) = chatDao.deleteChats(chats)

    suspend fun updateItem(item: Chat) = chatDao.update(item)

}


