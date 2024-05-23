package com.example.basic_ui_demo.companion.news

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NewsRetrofitInstance {
    //BASE_URL必须以/结尾
    private const val BASE_URL = "https://apis.tianapi.com/tiyu/"
    private const val BACKUP_BASE_URL = "https://apis.tianapi.com/football/"
    private const val API_KEY = "e27d3e1149c669a570070b4ac801de1a"

    private val apiKeyInterceptor = Interceptor { chain ->
        val originalRequest = chain.request()
        val newUrl = originalRequest.url.newBuilder()
            .addQueryParameter("key", API_KEY) // 将 API key 添加到 URL 中
            .build()
        val newRequest = originalRequest.newBuilder()
            .url(newUrl)
            .build()
        chain.proceed(newRequest)
    }

    private val client = OkHttpClient.Builder().apply {
        addInterceptor(apiKeyInterceptor)
    }.build()
    //构建OkHTTPClient对象，用于在每次使用api实例时添加api key

    var api: NewsInterface =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NewsInterface::class.java)

    fun changeApi(){
        api = Retrofit.Builder()
            .baseUrl(BACKUP_BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NewsInterface::class.java)
    }
}