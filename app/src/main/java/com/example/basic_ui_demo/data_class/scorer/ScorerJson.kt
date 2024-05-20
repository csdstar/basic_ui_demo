package com.example.basic_ui_demo.data_class.scorer

data class ScorerJson(
    val competition: Competition,
    val count: Int,
    val filters: Filters,
    val scorers: List<Scorer>,
    val season: Season
)