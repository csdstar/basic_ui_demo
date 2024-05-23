package com.example.footballapidemo.data_class.data

import com.example.basic_ui_demo.data_class.data.Stage

data class Standing(
    val group: Any,
    val stage: Stage,
    val table: List<Table>,
    val type: String
)