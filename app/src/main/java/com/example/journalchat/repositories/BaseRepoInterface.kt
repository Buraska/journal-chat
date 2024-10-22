package com.example.journalchat.repositories

interface BaseRepoInterface {

    fun getItems(): List<Any>

    fun getItem(id: String): Any?

    fun addItem(item: Any)

    fun updateItem(item: Any)

    fun deleteItem(item: Any)
}