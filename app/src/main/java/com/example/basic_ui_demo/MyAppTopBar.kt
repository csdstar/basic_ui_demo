package com.example.basic_ui_demo

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.example.basic_ui_demo.view.screen.Screen
import com.example.footballapidemo.data_class.data.Match
import com.example.basic_ui_demo.view_model.MatchesViewModel
import com.example.basic_ui_demo.view_model.MatchesViewModel.Companion.getMatchTitle
import com.example.basic_ui_demo.view_model.MatchesViewModel.Companion.isLeague
import com.example.basic_ui_demo.view_model.MatchesViewModel.Companion.match

val DetailScreenColor = Color(0xFF6612A3).copy(0.8f)

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MyAppTopBar(navController: NavController, nav: NavBackStackEntry?) {
    val curRoute = nav?.destination?.route
    when (curRoute) {
        Screen.NewsScreen.route -> GeneralTopBar(navController)
        Screen.DataScreen.route -> GeneralTopBar(navController)
        Screen.MatchDetailScreen.route -> MatchDetailTopBar(navController, match)
        Screen.StandingScreen.route -> StandingTopBar(navController)
        else -> {}
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GeneralTopBar(navController: NavController) {
    val nav = remember { navController.currentBackStackEntry }
    CenterAlignedTopAppBar(
        title = {
            NormalTextComponent(value = nav?.destination?.route.toString())
        },
        //左侧导航栏图标
        navigationIcon = {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                contentDescription = "",
                tint = Color.Black,
                modifier = Modifier.clickable {
                    navController.popBackStack()
                }
            )
        },
        //右侧图标
        actions = {
            Icon(
                imageVector = Icons.Outlined.Star,
                contentDescription = "",
            )
        },
        modifier = Modifier
            .height(30.dp)
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MatchDetailTopBar(navController: NavController, match: Match) {
    CenterAlignedTopAppBar(
        modifier = Modifier
            .height(40.dp),
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(DetailScreenColor),
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = getMatchTitle(match),
                    fontSize = 15.sp,
                    modifier = Modifier
                        .fillMaxHeight(0.7f),
                    textAlign = TextAlign.Center,
                    color = Color.White,
                )
                Spacer(Modifier.width(5.dp))
                if(isLeague(match)) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight(0.5f)
                            .aspectRatio(1f / 1f)
                            .background(Color.LightGray.copy(0.5f), shape = CircleShape)
                            .clickable {
                                navController.navigate(Screen.StandingScreen.route)
                                //导航至联赛积分榜
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                            contentDescription = "",
                            modifier = Modifier
                                .fillMaxSize(0.5f)
                        )
                    }
                }
            }
        },
        //左侧导航栏图标
        navigationIcon = {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                contentDescription = "",
                tint = Color.Black,
                modifier = Modifier.clickable {
                    navController.popBackStack()
                }
            )
        },
        //右侧图标
        actions = {
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = "",
                tint = Color.Black,
                modifier = Modifier.clickable { }
            )
        }
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StandingTopBar(navController: NavController) {
    CenterAlignedTopAppBar(
        title = {
            Text(text = MatchesViewModel.getCompetitionNameByMatch().toString(), color = Color.White)
        },
        //左侧导航栏图标
        navigationIcon = {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "",
                tint = Color.White,
                modifier = Modifier.clickable {
                    navController.popBackStack()
                }
            )
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(Color(0xFF313131))
    )
}