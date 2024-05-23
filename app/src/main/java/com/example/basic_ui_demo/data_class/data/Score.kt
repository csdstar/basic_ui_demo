package com.example.basic_ui_demo.data_class.data

import com.example.footballapidemo.data_class.data.FullTime
import com.example.footballapidemo.data_class.data.HalfTime

data class Score(
    val duration: String,
    val fullTime: FullTime,
    val halfTime: HalfTime,
    val winner: String
)