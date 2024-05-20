@file:RequiresApi(Build.VERSION_CODES.O)

package com.example.basic_ui_demo.view.data

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresExtension
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.basic_ui_demo.screen.TAG
import com.example.footballapidemo.data_class.data.Competition
import com.example.footballapidemo.view_model.MatchesViewModel

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun StandingScreen(competition: Competition?) {
    var selectedTab by remember { mutableStateOf("球队榜") }
    val selectedSeason = remember { mutableStateOf("2023") }

    if (competition != null)
        Column {
            DetailTabsBar(
                onTabSelected = { selectedTab = it },
                onSeasonSelect = { selectedSeason.value = it }
            )
            when (selectedTab) {
                "球队榜" -> TeamStandingCompose(competition, selectedSeason)
                "球员榜" -> ScorerStandingCompose(competition, selectedSeason)
            }
        }
}

//顶部菜单栏，包含一个选择season的下拉菜单和一个榜单选择指示器
//通过传入回调函数，将内部函数中选择的值传递到外部
@Composable
fun DetailTabsBar(
    onTabSelected: (String) -> Unit,
    onSeasonSelect: (String) -> Unit
) {

    val tabs = listOf("球队榜", "球员榜")

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .background(Color(0xFF555555)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {

        /////////////////////////////////////////////////////////////////////////////////////
        //选择赛季，下拉菜单
        var expanded by remember { mutableStateOf(false) }
        val seasons = listOf("2020", "2021", "2022", "2023")
        var selectedSeasonIndex by remember { mutableIntStateOf(3) }

        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Row(
                modifier = Modifier
                    .height(20.dp)
                    .clickable { expanded = true },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text =
                    if (selectedSeasonIndex >= 0) seasons[selectedSeasonIndex]
                    else MatchesViewModel.match.getCurrentSeasonYear().toString(),
                    color = Color.White,
                    fontSize = 15.sp,
                    textAlign = TextAlign.Center,
                )
                Spacer(Modifier.width(5.dp))
                Box(
                    modifier = Modifier
                        .aspectRatio(1f / 1f)
                        .background(Color.LightGray.copy(0.5f), shape = CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.KeyboardArrowDown,
                        contentDescription = "",
                        modifier = Modifier
                            .fillMaxSize(0.7f)
                    )
                }
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = {
                    expanded = false
                    Log.d(TAG, "close")
                },
                offset = DpOffset.Zero
            ) {
                seasons.forEachIndexed { index, item ->
                    DropdownMenuItem(
                        onClick = {
                            onSeasonSelect(item)
                            selectedSeasonIndex = index
                            expanded = false
                        },
                        text = { Text(text = item) }
                    )
                }
            }
        }

        ////////////////////////////////////////////////////////////////////////
        //选择榜单(球队榜、球员榜)
        var selectedTabIndex by remember { mutableIntStateOf(0) }
        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.Center
        ) {
            tabs.forEachIndexed { index, title ->
                val isSelected = index == selectedTabIndex
                val backgroundColor by animateColorAsState(
                    targetValue = if (isSelected) Color.DarkGray else Color.Gray,
                    animationSpec = spring(),
                    label = ""
                )
                Box(
                    modifier = Modifier
                        .wrapContentWidth()
                        .weight(0.3f)
                        .border(BorderStroke(1.dp, Color.Black))
                        .background(color = backgroundColor, shape = RoundedCornerShape(4.dp))
                        .clickable {
                            onTabSelected(title)
                            selectedTabIndex = index
                        }
                ) {
                    Text(
                        text = title,
                        color = Color.White,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        }
    }
}

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Preview
@Composable
fun StandingScreenPreview() {
    StandingScreen(competition = null)
}