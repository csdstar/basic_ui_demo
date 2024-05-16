package com.example.basic_ui_demo.news

import com.example.footballapidemo.data_class.news.Result

data class NewsJson(
    val code: Int,
    val msg: String,
    val result: Result?
)