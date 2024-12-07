package com.example.journalchat.data.repositories

import com.example.journalchat.data.daos.MessageDao
import com.example.journalchat.data.models.Message
import kotlinx.coroutines.flow.Flow


class MessageRepository(private val messageDao: MessageDao) {

    fun getAllStream(): Flow<List<Message>> = messageDao.getAll()

    fun getStream(id: Int): Flow<Message?> = messageDao.get(id)

    suspend fun insertItem(item: Message) = messageDao.insert(item)

    suspend fun deleteItem(item: Message) = messageDao.delete(item)

    suspend fun updateItem(item: Message) = messageDao.update(item)
}