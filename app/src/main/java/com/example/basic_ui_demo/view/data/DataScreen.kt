package com.example.basic_ui_demo.view.data

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.basic_ui_demo.view_model.DataViewModel
import com.lt.compose_views.compose_pager.ComposePager
import com.lt.compose_views.compose_pager.rememberComposePagerState
import com.lt.compose_views.pager_indicator.TextPagerIndicator

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DataScreen(navController: NavController, viewModel: DataViewModel){
    val competitions = DataViewModel.competitions
    val competitionCode = DataViewModel.competitionsCode

    val composePagerState = rememberComposePagerState()
    //pagerState
    val curIndex by remember { composePagerState.getCurrSelectIndexState() }
    //pager的当前index

    LaunchedEffect(curIndex){
        viewModel.setIndex(curIndex)
    }

    Column {
        TextPagerIndicator(
            texts = competitions,
            offsetPercentWithSelectFlow = composePagerState.createChildOffsetPercentFlow(),
            selectIndexFlow = composePagerState.createCurrSelectIndexFlow(),
            fontSize = 16.sp,
            selectFontSize = 16.sp,
            textColor = Color.Black,
            selectTextColor = Color.Green,
            selectIndicatorColor = Color.Green,
            onIndicatorClick = { composePagerState.setPageIndexWithAnimate(it) },
            modifier = Modifier
                .fillMaxWidth()
                .height(30.dp),
            userCanScroll = true,
            margin = 20.dp
        )

        ComposePager(
            pageCount = competitions.size,
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.CenterHorizontally),
            composePagerState = composePagerState,
            orientation = Orientation.Horizontal,
            pageCache = 1,
        ) {
            StandingScreen(viewModel,curIndex)
        }
    }
}