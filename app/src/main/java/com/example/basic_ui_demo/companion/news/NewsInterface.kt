package com.example.basic_ui_demo.companion.news

import com.example.basic_ui_demo.data_class.news.NewsJson
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsInterface {
    @GET("index")
    suspend fun getNews(
        @Query("rand") rand: Int = 1,
        @Query("num") num: Int = 10
    ): Response<NewsJson>
}