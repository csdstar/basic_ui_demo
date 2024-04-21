package com.example.basic_ui_demo

import android.content.Context
import android.graphics.drawable.Icon
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.currentCoroutineContext

@Composable
fun MyAppBottomBar(navController: NavController){
    BottomAppBar (
        modifier = Modifier
    ){
        Row(
            modifier = Modifier.height(20.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Box(modifier = Modifier.weight(1F)){
                IconAndText(
                    modifier = Modifier.clickable {
                        navController.navigate(Screen.NewsScreen.route)
                    },
                    imageVector = Icons.Rounded.AccountBox,
                    text = stringResource(id = R.string.news)
                )
            }

            Box(modifier = Modifier.weight(1F)){
                IconAndText(
                    modifier = Modifier.clickable {
                        navController.navigate(Screen.MatchesScreen.route)
                    },
                    imageVector = Icons.Rounded.AddCircle,
                    text = stringResource(id = R.string.match)
                )
            }

            Box(modifier = Modifier.weight(1F)){
                IconAndText(
                    modifier = Modifier.clickable {
                        navController.navigate(Screen.TeamsScreen.route)
                    },
                    imageVector = Icons.Rounded.Call,
                    text = stringResource(id = R.string.team)
                )
            }
            Box(modifier = Modifier.weight(1F)){
                IconAndText(
                    modifier = Modifier.clickable {
                        navController.navigate(Screen.DataScreen.route)
                    },
                    imageVector = Icons.Rounded.CheckCircle,
                    text = stringResource(id = R.string.data)
                )
            }

        }
    }
}

@Composable
fun IconAndText(
    modifier: Modifier = Modifier,
    imageVector: ImageVector,
    text: String
){
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxWidth()
    ){
        Column {
            Icon(
                modifier = modifier,
                imageVector = imageVector,
                contentDescription = ""
            )
            Text(text = text)
        }
    }
}

@Preview
@Composable
fun PreviewIconAndText(){
    IconAndText(
        imageVector = Icons.Rounded.AddCircle,
        text = stringResource(id = R.string.match)
    )
}


@Preview
@Composable
fun PreviewBottomBar(){
    MyAppBottomBar(navController = NavController(LocalContext.current))
}