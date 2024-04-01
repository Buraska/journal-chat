package com.example.journalchat.models

object Data {

    val c1: Chat = Chat(
        "Topics discssing of Mobseratte",
        listOf(Message("I wonder how long have we seen each others. For some reasons...")),
        null)

    val c2: Chat = Chat(
        "Lorem Ipsum",
        listOf(Message("Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galle")),
        null)

    val c3: Chat = Chat(
        "Lorem Ipsuasdddddddddddddddddddddddddddddddddddm",
        listOf(Message("It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout. The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using 'Content here, content here', making it look like readable English. Many desktop publishing packages and web page editors now use Lorem Ipsum as their default model text, and a search for 'lorem ipsum' will uncover many web sites still in their infancy. Various versions have evolved over the years, sometimes by accident, sometimes on purpose (injected humour and the like).\n")),
        null)

    val chatList: List<Chat> = listOf(c1.copy(), c2.copy(), c3.copy(), c1.copy(), c2.copy(), c3.copy(),c1.copy(), c2.copy(), c3.copy(),c1.copy(), c2.copy(), c3.copy(),c1.copy(), c2.copy(), c3.copy())

}