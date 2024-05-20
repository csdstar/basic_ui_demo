package com.example.footballapidemo.data_class.data

data class Standing(
    val group: Any,
    val stage: Stage,
    val table: List<Table>,
    val type: String
)