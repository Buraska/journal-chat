package com.example.journalchat.data.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.journalchat.data.models.Message
import com.example.journalchat.data.models.Tag
import kotlinx.coroutines.flow.Flow


@Dao
interface TagDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: Tag)

    @Transaction()
    suspend fun insertAll(tags: List<Tag>){
        for (tag in tags){
            insert(tag)
        }
    }

    @Update
    suspend fun update(item: Tag)

    @Delete
    suspend fun delete(item: Tag)

    @Query("SELECT * from tag WHERE id = :id")
    fun get(id: Long): Flow<Tag>

    @Query("SELECT * from tag ORDER BY lastAccess DESC")
    fun getAll(): Flow<List<Tag>>

}