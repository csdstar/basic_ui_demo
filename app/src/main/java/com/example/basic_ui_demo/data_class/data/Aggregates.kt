package com.example.basic_ui_demo.data_class.data

import com.example.footballapidemo.data_class.data.AwayTeam
import com.example.footballapidemo.data_class.data.HomeTeam

data class Aggregates(
    val awayTeam: AwayTeam,
    val homeTeam: HomeTeam,
    val numberOfMatches: Int,
    val totalGoals: Int
)

//统计量