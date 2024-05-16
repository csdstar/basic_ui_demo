package com.example.footballapidemo.data_class.data

data class Score(
    val duration: String,
    val fullTime: FullTime,
    val halfTime: HalfTime,
    val winner: Any
)