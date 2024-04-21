package com.example.basic_ui_demo

import androidx.compose.ui.res.stringResource

sealed class Screen(val route:String) {
    data object MainScreen: Screen("MainScreen")
    data object TestScreen: Screen("TestScreen")
    data object NewsScreen: Screen("News")
    data object MatchesScreen: Screen("Matches")
    data object TeamsScreen: Screen("Teams")
    data object DataScreen: Screen("Data")
}