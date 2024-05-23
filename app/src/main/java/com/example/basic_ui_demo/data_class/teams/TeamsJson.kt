package com.example.basic_ui_demo.data_class.teams

data class TeamsJson(
    val competition: Competition,
    val count: Int,
    val filters: Filters,
    val season: Season,
    val teams: List<Team>
)