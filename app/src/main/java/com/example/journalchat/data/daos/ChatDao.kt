package com.example.journalchat.data.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.journalchat.data.models.Chat
import com.example.journalchat.data.models.Message
import kotlinx.coroutines.flow.Flow


@Dao
interface ChatDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: Chat)

    @Update
    suspend fun update(item: Chat)

    @Delete
    suspend fun delete(item: Chat)

    @Transaction
    suspend fun deleteChats(chats: List<Chat>)
    {
        for (chat in chats){
            delete(chat)
        }
    }

    @Query("SELECT * from chat WHERE id = :id")
    fun get(id: Long): Flow<Chat>

    @Query("SELECT * from chat")
    fun getAll(): Flow<List<Chat>>

    @Query("SELECT * FROM chat JOIN message ON chat.id = message.chat_id WHERE chat.id = :id")
    fun getChatAndMessages(id: Int): Map<Chat, List<Message>>
}