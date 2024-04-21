package com.example.basic_ui_demo

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    
    val navController = rememberNavController()
    Log.d("MyTag",navController.currentDestination?.route.toString())
    Scaffold(
        // 让Bar为一个运行了组合函数的lambda，这样可以传入附加参数
        topBar = {
            when(navController.currentDestination?.route){
                Screen.NewsScreen.route -> {}
                else -> MyAppTopBar(title = stringResource(id = R.string.home))
            }
        },
        bottomBar = { MyAppBottomBar(navController) }
    ){
        paddingValues ->
        // 主要内容区域
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ){
            NavHost(
                navController = navController,
                startDestination = Screen.NewsScreen.route
            ){
                composable(Screen.NewsScreen.route){
                    NewsScreen(navController = navController)
                }
                composable(Screen.MatchesScreen.route){
                    MatchesScreen(navController = navController)
                }
                composable(Screen.TeamsScreen.route){
                    TeamsScreen(navController = navController)
                }
                composable(Screen.DataScreen.route){
                    DataScreen(navController = navController)
                }
                composable(Screen.TestScreen.route){
                    TestScreen(navController = navController)
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewMainScreen() {
    MainScreen()
}