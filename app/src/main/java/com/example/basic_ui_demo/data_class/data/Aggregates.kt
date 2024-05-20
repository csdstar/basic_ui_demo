package com.example.basic_ui_demo.data_class.data

import com.example.footballapidemo.data_class.data.AggregatesTeam

data class Aggregates(  //统计量
    val awayTeam: AggregatesTeam,
    val homeTeam: AggregatesTeam,
    val numberOfMatches: Int,
    val totalGoals: Int
) {
    fun returnTeam(isHome: Boolean): AggregatesTeam{
        return if (isHome) homeTeam
        else awayTeam
    }
}


