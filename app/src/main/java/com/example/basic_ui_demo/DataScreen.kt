package com.example.basic_ui_demo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController

@Composable
fun DataScreen(navController: NavController){
    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Magenta)
    ){
        NormalTextComponent(value = "DataScreen")
    }
}