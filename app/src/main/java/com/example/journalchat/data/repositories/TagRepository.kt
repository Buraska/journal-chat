package com.example.journalchat.data.repositories

import com.example.journalchat.data.daos.TagDao
import com.example.journalchat.data.models.Message
import com.example.journalchat.data.models.Tag
import kotlinx.coroutines.flow.Flow


class TagRepository(private val tagDao: TagDao) {

    fun getAllStream(): Flow<List<Tag>> = tagDao.getAll()

    fun getStream(id: Long): Flow<Tag?> = tagDao.get(id)

    suspend fun insertItem(item: Tag) = tagDao.insert(item)

    suspend fun deleteItem(item: Tag) = tagDao.delete(item)

    suspend fun deleteTag(tag: Tag) = tagDao.delete(tag)

    suspend fun updateItem(tag: Tag) = tagDao.update(tag)
}