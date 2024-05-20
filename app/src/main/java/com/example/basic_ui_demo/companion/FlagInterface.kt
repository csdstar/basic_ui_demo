package com.example.basic_ui_demo.companion

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface FlagInterface {
    @GET("name/{nationName}")
    suspend fun getFlagPicUrl(
        @Path("nationName") nationName: String
    ): Response<String>
}