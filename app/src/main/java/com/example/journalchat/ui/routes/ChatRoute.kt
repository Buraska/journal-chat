package com.example.journalchat.ui.routes

import androidx.compose.runtime.Composable
import com.example.journalchat.models.Message
import com.example.journalchat.ui.ChatScreen
import com.example.journalchat.ui.states.ChatState
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun ChatRoute(navigateUp: () -> Unit) {
    val messages = listOf<Message>(
        Message("Starting: Intent { act=android.intent.action.MAIN cat=[android.intent.category.LAUNCHER] cmp=com.example.journalchat/.MainActivity }",
            dateTime = LocalDateTime.parse("2012-12-24 12:24:35", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))),
        Message("Starting: =NCH"),
        Message("Starting: Intent { act=android.intent.action.MAIN cat=[android.intent.category.LAUNCHER] cmp=com.example.journalchat/.MainActivity }"),
        Message("Startinge.journalchat/.MainActivity }",
            isPrimary = false),
        Message("ASD most often refers to: Autism spectrum disorder, a neurodevelopmental condition. Читать ещё"),
        Message("Подписчиков: 2,8 тыс.О себе: Группа компаний ASD - правообладатель зарегистрированных торговых марок «Секунда» (клеи для ремонта), Paterra (товары для дома), Aviora..."
            ,isPrimary = false),
        Message("Подписчиков: 2,8 тыс.О себе: Группа компаний ASD - правообладатель зарегистрированных торговых марок «Секунда» (клеи для ремонта), Paterra (товары для дома), Aviora..."),
        Message("Подписчиков: 2,8 тыс.О себе: Группа компаний ASD - правообладатель зарегистрированных торговых марок «Секунда» (клеи для ремонта), Paterra (товары для дома), Aviora...",
            isPrimary = false),
        Message("Подписчиков: 2,8 тыс.О себе: Группа компаний ASD - правообладатель зарегистрированных торговых марок «Секунда» (клеи для ремонта), Paterra (товары для дома), Aviora..."),
    )
    val chatState = ChatState("FirstChat", messages)
    ChatScreen(chatState = chatState, navigateUp = { /*TODO*/ })


}