package com.example.journalchat.dataflowTest

import com.example.journalchat.data.repositories.BaseRepo
import com.example.journalchat.data.repositories.BaseRepoInterface
import com.example.journalchat.data.repositories.ChatRepo
import kotlinx.coroutines.flow.flow

class FakeRepo: BaseRepoInterface {

    fun observeCount() = flow {
        emit(1)
    }

    override fun getItems(): List<Any> {
        TODO("Not yet implemented")
    }

    override fun getItem(id: String): Any? {
        TODO("Not yet implemented")
    }

    override fun addItem(item: Any) {
        TODO("Not yet implemented")
    }

    override fun updateItem(item: Any) {
        TODO("Not yet implemented")
    }

    override fun deleteItem(item: Any) {
        TODO("Not yet implemented")
    }
}