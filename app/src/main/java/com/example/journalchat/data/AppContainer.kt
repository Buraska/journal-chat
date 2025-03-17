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
import com.example.journalchat.data.repositories.ChatRepository
import com.example.journalchat.data.repositories.MessageRepository
import com.example.journalchat.data.repositories.TagRepository

/**
 * App container for Dependency injection.
 */


class AppDataContainer(private val context: Context){

     val chatRepository: ChatRepository by lazy {
         ChatRepository(JournalChatDatabase.getDatabase(context).chatDao())
    }

    val messageRepository: MessageRepository by lazy {
        MessageRepository(JournalChatDatabase.getDatabase(context).messageDao())
    }

    val tagRepository: TagRepository by lazy {
        TagRepository(JournalChatDatabase.getDatabase(context).tagDao())
    }
}
