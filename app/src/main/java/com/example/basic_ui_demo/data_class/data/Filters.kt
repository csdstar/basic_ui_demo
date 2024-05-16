package com.example.footballapidemo.data_class.data

data class Filters(
    val id : Int?,
    val matchday : Int?,
    val season: Int?,
    val competitions: String?,
    val limit: Int?,
    val permission: String?,
    val offset : Int?,
    val status : String?,
    val venue : Venue?,
    val date : String?,
    val dateFrom : String?,
    val dateTo : String?
)