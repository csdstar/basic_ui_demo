package com.example.footballapidemo.data_class.data

import com.example.basic_ui_demo.data_class.data.Aggregates

data class Head2headJson(
    val aggregates: Aggregates,  //统计胜负结果
    val filters: Filters,
    val matches: List<Match>,  //同联赛下前若干场的记录
    val resultSet: ResultSet
)