package com.example.basic_ui_demo

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountBox
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material.icons.rounded.Call
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.basic_ui_demo.screen.Screen

@Composable
fun MyAppBottomBar(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    when(navBackStackEntry?.destination?.route.toString()) {
        Screen.MatchDetailScreen.route -> {}
        else -> BottomAppBar1(navController)
    }
}

@Composable
fun BottomAppBar1(navController: NavController){
    BottomAppBar(
        modifier = Modifier
    ) {
        Row(
            modifier = Modifier.height(20.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Box(modifier = Modifier.weight(1F)) {
                IconAndText(
                    modifier = Modifier,
                    navController = navController,
                    imageVector = Icons.Rounded.AccountBox,
                    destinationRoute = Screen.NewsScreen.route
                )
            }

            Box(modifier = Modifier.weight(1F)) {
                IconAndText(
                    modifier = Modifier,
                    navController = navController,
                    imageVector = Icons.Rounded.AddCircle,
                    destinationRoute = Screen.MatchesScreen.route,
                    navOptions = NavOptions.Builder().setLaunchSingleTop(true).build()
                )
            }

            Box(modifier = Modifier.weight(1F)) {
                IconAndText(
                    modifier = Modifier,
                    navController = navController,
                    imageVector = Icons.Rounded.Call,
                    destinationRoute = Screen.TeamsScreen.route
                )
            }
            Box(modifier = Modifier.weight(1F)) {
                IconAndText(
                    modifier = Modifier,
                    navController = navController,
                    imageVector = Icons.Rounded.CheckCircle,
                    destinationRoute = Screen.DataScreen.route
                )
            }

        }
    }
}

@Composable
fun IconAndText(
    modifier: Modifier = Modifier,
    navController: NavController,
    imageVector: ImageVector,
    destinationRoute: String,
    navOptions: NavOptions? = null
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                navController.navigate(destinationRoute, navOptions)
            }
    ) {
        Column {
            Icon(
                modifier = modifier,
                imageVector = imageVector,
                contentDescription = ""
            )
            Text(text = destinationRoute)
        }
    }
}