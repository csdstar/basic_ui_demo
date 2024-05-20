package com.example.basic_ui_demo.data_class.scorer

data class Scorer(
    val assists: Int,
    val goals: Int,
    val penalties: Int,
    val playedMatches: Int,
    val player: Player,
    val team: Team
)