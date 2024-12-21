package com.example.journalchat.data.repositories

import com.example.journalchat.data.daos.MessageDao
import com.example.journalchat.data.models.Message
import kotlinx.coroutines.flow.Flow


class MessageRepository(private val messageDao: MessageDao) {

    fun getAllStream(id: Long): Flow<List<Message>> = messageDao.getAll(id)

    fun getStream(id: Long): Flow<Message?> = messageDao.get(id)

    suspend fun insertItem(item: Message) = messageDao.insert(item)

    suspend fun deleteItem(item: Message) = messageDao.delete(item)

    suspend fun deleteMessages(messages: List<Message>) = messageDao.deleteMessages(messages)

    suspend fun updateItem(item: Message) = messageDao.update(item)
}