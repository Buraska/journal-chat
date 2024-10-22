package com.example.journalchat.dataflowTest

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
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
}

class NewsRepository(private val newsRemoteDataSource: NewsRemoteDataSource)
{
    val oddNews: Flow<String> = newsRemoteDataSource.latestNews.filter { "\\d+".toRegex().find(it)!!.value.toInt() % 2 == 1 }

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
        NewsViewModel(NewsRepository(NewsRemoteDataSource()))
    }
}

