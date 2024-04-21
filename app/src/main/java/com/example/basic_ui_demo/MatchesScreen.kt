package com.example.basic_ui_demo

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController

@Composable
fun MatchesScreen(navController: NavController){
    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Red)
    ){
        Log.d("MyTag",navController.currentDestination?.route.toString())
        NormalTextComponent(value = "MatchesScreen")
    }
}