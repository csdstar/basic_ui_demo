package com.example.basic_ui_demo.companion

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object FlagRetrofitInstance {
    private const val BASE_URL = "https://restcountries.com/v3.1/"

    private val client = OkHttpClient.Builder().apply {
        addInterceptor(Interceptor { chain ->
            val request = chain.request().newBuilder().build()
            chain.proceed(request)
        })
    }.build()
    //构建OkHTTPClient对象

    val api: FlagInterface by lazy {
        Retrofit.Builder()
            .baseUrl(FlagRetrofitInstance.BASE_URL)
            .client(FlagRetrofitInstance.client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(FlagInterface::class.java)
    }
}