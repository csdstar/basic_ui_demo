package com.example.basic_ui_demo

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyAppTopBar(title: String){
    
    TopAppBar(
        title = {
            NormalTextComponent(value = title)
        },
        //左侧导航栏图标
        navigationIcon = {
            Icon(
                imageVector = Icons.Filled.Menu,
                contentDescription = stringResource(id = R.string.menu),
                tint = Color.Black
            )
        },
        //右侧图标
        actions = {
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = ""
            )
        }
    )
}