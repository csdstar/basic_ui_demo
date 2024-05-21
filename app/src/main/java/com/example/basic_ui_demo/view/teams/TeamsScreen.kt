package com.example.basic_ui_demo.view.teams

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.basic_ui_demo.NormalTextComponent
import com.example.basic_ui_demo.view_model.TeamsViewModel
import com.lt.compose_views.compose_pager.rememberComposePagerState

@Composable
fun TeamsScreen(navController: NavController,viewModel: TeamsViewModel) {
    val composePagerState = rememberComposePagerState()
    //pagerState
    val curIndex by remember { composePagerState.getCurrSelectIndexState() }
    //pager的当前index

    LaunchedEffect(curIndex) {
        viewModel.setIndex(curIndex)
    }


    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Gray)
    ){
        NormalTextComponent(value = "TeamsScreen")
    }
}