package com.example.basic_ui_demo

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController

@Composable
fun NewsScreen(navController: NavController){
    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Green)
    ){
        NormalTextComponent(value = "NewsScreen")
        Button(
            onClick = {
                navController.navigate(Screen.TestScreen.route)
            }
        ) {
            Log.d("MyTag",navController.currentDestination?.route.toString())
            Text(text = "test")
        }
    }
}