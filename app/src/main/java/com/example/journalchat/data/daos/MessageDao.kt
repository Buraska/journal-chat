package com.example.journalchat.data.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
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

    @Query("SELECT * from message WHERE chat_id = :chatId AND referenceId IS NULL ORDER BY date DESC")
    fun getAllWithoutReferences(chatId: Long): Flow<List<Message>>

    @Query("SELECT * from message ORDER BY date DESC")
    fun getAll(): Flow<List<Message>>

    @Query("SELECT * from message WHERE referenceId = :referenceId ORDER BY date DESC")
    fun getAllFilterByReferenceId(referenceId: Long): Flow<List<Message>>

    @Transaction
    suspend fun deleteMessages(messages: List<Message>)
    {
        for (message in messages){
            delete(message)
        }
    }
}