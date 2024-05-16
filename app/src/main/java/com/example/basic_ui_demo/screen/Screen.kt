package com.example.basic_ui_demo.screen

sealed class Screen(val route:String) {
    data object MainScreen: Screen("MainScreen")
    data object TestScreen: Screen("Test")
    data object NewsScreen: Screen("News")
    data object MatchesScreen: Screen("Matches")
    data object TeamsScreen: Screen("Teams")
    data object DataScreen: Screen("Data")
    data object MatchDetailScreen: Screen("MatchDetail")
}