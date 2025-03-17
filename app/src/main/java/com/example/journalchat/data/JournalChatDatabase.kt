/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.journalchat.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.journalchat.data.daos.ChatDao
import com.example.journalchat.data.daos.MessageDao
import com.example.journalchat.data.daos.TagDao
import com.example.journalchat.data.models.Chat
import com.example.journalchat.data.models.Message
import com.example.journalchat.data.models.Tag
import java.time.LocalDateTime

@Database(entities = [Chat::class, Message::class, Tag::class], version = 2, exportSchema = true)
@TypeConverters(Converters::class)
abstract class JournalChatDatabase : RoomDatabase() {

    abstract fun messageDao(): MessageDao
    abstract fun chatDao(): ChatDao
    abstract fun tagDao(): TagDao

    companion object {
        @Volatile
        private var Instance: JournalChatDatabase? = null

        fun getDatabase(context: Context): JournalChatDatabase {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, JournalChatDatabase::class.java, "journal_chat_database")
                    .fallbackToDestructiveMigration()
//                    .createFromAsset("database/tag.db")
                    .build().also { Instance = it }
            }
        }
    }
}


fun parseEmojiCsv(context: Context, fileName: String): List<Tag>{
    val tagList = mutableListOf<Tag>()
    context.assets.open("database/emojis.csv")
    tagList.add(Tag(0, "ASDASD", LocalDateTime.now(), "TEST"))

    return tagList
}

class Converters {
    @TypeConverter
    fun fromTimestamp(value: String?): LocalDateTime? {
        return value?.let { LocalDateTime.parse(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: LocalDateTime?): String? {
        return date?.toString()
    }
}


