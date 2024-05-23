package com.example.basic_ui_demo.view.teams

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
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
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.basic_ui_demo.view.screen.TAG
import com.example.basic_ui_demo.view_model.TeamsViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SeasonHeader(
    initIndex: Int = 3,
    onSeasonSelect: (String) -> Unit
) {
    //用于选择赛季的header

    var expanded by remember { mutableStateOf(false) }
    val seasons = TeamsViewModel.seasons
    var selectedSeasonIndex by remember { mutableIntStateOf(initIndex) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(Color.White),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {


        Row(
            modifier = Modifier.height(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            //上一赛季按钮
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier
                    .weight(1f)
                    .clickable {
                        if ((selectedSeasonIndex - 1) in seasons.indices)
                            onSeasonSelect(seasons[--selectedSeasonIndex])
                    }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                    contentDescription = ""
                )
                Text(text = "上一赛季")
            }

            //下拉菜单按钮
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .weight(2f),
            ) {
                Row {
                    Text(
                        text = seasons[selectedSeasonIndex],
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center,
                        color = Color.Black,
                        modifier = Modifier.clickable { expanded = true }
                    )
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .aspectRatio(1f / 1f)
                            .padding(3.dp)
                            .background(Color.LightGray, shape = CircleShape)
                            .clickable { expanded = true }
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.KeyboardArrowDown,
                            contentDescription = "",
                            modifier = Modifier.fillMaxHeight()
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
            //下一赛季按钮
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End,
                modifier = Modifier
                    .weight(1f)
                    .clickable {
                        if ((selectedSeasonIndex + 1) in seasons.indices)
                            onSeasonSelect(seasons[++selectedSeasonIndex])
                    }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                    contentDescription = ""
                )
                Text(text = "下一赛季")
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SeasonHeader(
    initSeason: String,
    onSeasonSelect: (String) -> Unit
) {
    val seasons = TeamsViewModel.seasons
    val initIndex = seasons.indexOf(initSeason)
    SeasonHeader(initIndex, onSeasonSelect)
}