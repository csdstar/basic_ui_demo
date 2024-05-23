package com.example.basic_ui_demo.view.screen

sealed class Screen(val route:String) {
    data object MainScreen: Screen("MainScreen")
    data object NewsScreen: Screen("News")
    data object MatchesScreen: Screen("Matches")
    data object TeamsScreen: Screen("Teams")
    data object TeamDetailScreen : Screen("TeamDetail/{teamId}") {
        fun createRoute(teamId: Int) = "TeamDetail/$teamId"
    }
    data object DataScreen: Screen("Data")
    data object MatchDetailScreen: Screen("MatchDetail")
    data object ScoreScreen: Screen("Score")
    data object  StandingScreen: Screen("standing")
}