package com.example.journalchat.data.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.journalchat.data.models.Message
import kotlinx.coroutines.flow.Flow


@Dao
interface MessageDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: Message)

    @Update
    suspend fun update(item: Message)

    @Delete
    suspend fun delete(item: Message)

    @Query("SELECT * from message WHERE id = :id")
    fun get(id: Long): Flow<Message>

    @Query("SELECT * from message WHERE chat_id = :chatId ORDER BY date DESC")
    fun getAll(chatId: Long): Flow<List<Message>>
}