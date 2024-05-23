package com.example.basic_ui_demo.view_model

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import com.example.basic_ui_demo.data_class.news.News

class NewsViewModel: ViewModel() {
    private val _newsList = mutableStateListOf<News>()
    val newsList: SnapshotStateList<News>
        get()= _newsList
    fun addNews(news: News){
        _newsList.add(news)
    }

    fun addNewsFront(news: News){
        _newsList.add(0,news)
    }
}