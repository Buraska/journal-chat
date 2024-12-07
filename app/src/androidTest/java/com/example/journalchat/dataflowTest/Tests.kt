package com.example.journalchat.dataflowTest

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.invoke
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Test
class NewsRemoteDataSource(
    private val refreshIntervalMs: Long = 5000,
    private var counter: Int = 0
)
{
    val latestNews: Flow<String> = flow {
        while (true) {
            counter++
            val news = "news$counter"
            emit(news)
            delay(refreshIntervalMs)
        }
    }

    val flow = MutableStateFlow<String>("asd")

    suspend fun castFireBall(value: String) = flow.emit(value)
}

class NewsRepository(private val newsRemoteDataSource: NewsRemoteDataSource)
{
    val oddNews: Flow<String> = newsRemoteDataSource.latestNews.filter { "\\d+".toRegex().find(it)!!.value.toInt() % 2 == 1 }

    fun flow(): Flow<String> {
        val value = newsRemoteDataSource.flow
        println(value.value)
        return value
    }
}

class NewsViewModel(private val newsRepository: NewsRepository) : ViewModel()
{
    init {
        viewModelScope.launch {
            newsRepository.oddNews.collect { news -> println(news) };
        }
    }
    val oddNews: Flow<String> = newsRepository.oddNews
}

class Tests {

    @Test
    fun myTest() {

        runBlocking {
            println(NewsRepository(NewsRemoteDataSource()).oddNews.first())
        }
    }

    @Test
    fun backgroundScopeTest() = runTest{
        val dataSource = NewsRemoteDataSource()
        val repo = NewsRepository(dataSource)

        val values = mutableListOf<String>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)){
            println(repo.flow().collect())
        }
        dataSource.castFireBall("Fireball!")
        dataSource.castFireBall("Fireball!")
        dataSource.castFireBall("Fireball!")

    }
}

