package com.example.basic_ui_demo.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.basic_ui_demo.MyAppBottomBar
import com.example.basic_ui_demo.MyAppTopBar
import com.example.basic_ui_demo.view.data.DataScreen
import com.example.basic_ui_demo.view.data.StandingScreen
import com.example.basic_ui_demo.view.matches.MatchDetailScreen
import com.example.basic_ui_demo.view.matches.MatchesScreen
import com.example.basic_ui_demo.view.teams.TeamsScreen
import com.example.basic_ui_demo.view_model.DataViewModel
import com.example.basic_ui_demo.view_model.TeamsViewModel
import com.example.footballapidemo.view_model.MatchesViewModel

const val TAG = "MyTag"

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val navState by navController.currentBackStackEntryAsState()
    val matchesViewModel = MatchesViewModel()
    val dataViewModel = DataViewModel()
    val teamsViewModel = TeamsViewModel()

    Scaffold(
        // 让Bar为一个运行了组合函数的lambda，这样可以传入附加参数
        topBar = {
            MyAppTopBar(navController, navState)
        },
        bottomBar = { MyAppBottomBar(navController) }
    ) { paddingValues ->
        // 主要内容区域
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            NavHost(
                navController = navController,
                startDestination = Screen.NewsScreen.route
            ) {
                composable(Screen.NewsScreen.route) {
                    NewsScreen(navController = navController)
                }
                composable(Screen.MatchesScreen.route) {
                    MatchesScreen(navController = navController, viewModel = matchesViewModel)
                }
                composable(Screen.MatchDetailScreen.route) {
                    MatchDetailScreen()
                }
                composable(Screen.TeamsScreen.route) {
                    TeamsScreen(navController = navController, viewModel = teamsViewModel)
                }
                composable(Screen.DataScreen.route) {
                    DataScreen(navController = navController, viewModel = dataViewModel)
                }
                composable(Screen.TestScreen.route) {
                    TestScreen(navController = navController)
                }
                composable(Screen.StandingScreen.route) {
                    StandingScreen(competition = MatchesViewModel.getCurrentCompetitionByMatch())
                }
            }
        }
    }
}
