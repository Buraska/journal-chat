package com.example.journalchat.data

import com.example.journalchat.models.Chat
import com.example.journalchat.models.Message

object Data {

    val c1: Chat = Chat(
        "Topics discssing of Mobseratte",
        mutableListOf(Message("I wonder how long have we seen each others. For some reasons...")),
        null)

    val c2: Chat = Chat(
        "Lorem Ipsum",
        mutableListOf(Message("Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galle")),
        null)

    val c3: Chat = Chat(
        "Lorem Ipsuasdddddddddddddddddddddddddddddddddddm",
        mutableListOf(Message("It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout. The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using 'Content here, content here', making it look like readable English. Many desktop publishing packages and web page editors now use Lorem Ipsum as their default model text, and a search for 'lorem ipsum' will uncover many web sites still in their infancy. Various versions have evolved over the years, sometimes by accident, sometimes on purpose (injected humour and the like).\n")),
        null)

    val chatList: MutableList<Chat> = mutableListOf(
        c1.copy(), c2.copy(), c3.copy(), c1.copy(), c2.copy(), c3.copy(),
        c1.copy(), c2.copy(), c3.copy(),
        c1.copy(), c2.copy(), c3.copy(),
        c1.copy(), c2.copy(), c3.copy())

}