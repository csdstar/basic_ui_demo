package com.example.basic_ui_demo.view.matches

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.basic_ui_demo.companion.ApiViewModel
import com.example.basic_ui_demo.companion.RetrofitInstance
import com.example.basic_ui_demo.companion.convertUtcToChinaDate
import com.example.basic_ui_demo.companion.convertUtcToChinaTime
import com.example.basic_ui_demo.data_class.data.Status
import com.example.footballapidemo.data_class.data.Match
import com.example.footballapidemo.data_class.data.Team
import com.example.footballapidemo.view_model.MatchesViewModel

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MatchDetailScreen() {
    val match = MatchesViewModel.match
    var isLoading by remember { mutableStateOf(false) }
    var isError by remember { mutableStateOf(false) }
    val list = remember { mutableStateListOf<Match>() }

    LaunchedEffect(Unit) {
        isLoading = true
        val api = RetrofitInstance.api
        val head2headJson = ApiViewModel.callApi {
            api.getHead2Head(match.id)
        }
        if (head2headJson == null)
            isError = true
        else {
            list.addAll(head2headJson.matches)
            list.sortBy { it.convertUtcToChinaLocalDate() }
        }
        isLoading = false
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(Color.LightGray),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            TeamDetailBox(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .wrapContentHeight(),
                team = match.homeTeam
            )

            // 中间的 CenterDetailBox
            Layout(
                modifier = Modifier.weight(1f),
                content = {
                    CenterDetailBox(
                        modifier = Modifier.fillMaxWidth(),
                        match = match
                    )
                }
            ) { measures, constraints ->
                val placeable = measures.first().measure(constraints)
                layout(placeable.width, placeable.height) {
                    placeable.place(0, 0)
                }
            }

            TeamDetailBox(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .wrapContentHeight(),
                team = match.awayTeam
            )
        }
        Text(
            text = "以往比赛",
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Blue),
            textAlign = TextAlign.Center
        )
        if (isLoading) {
            CircularProgressIndicator()
        } else if (isError) {
            Column(Modifier.fillMaxWidth()) {
                Icon(
                    imageVector = Icons.Default.Warning, // 使用警告图标
                    contentDescription = "Error",
                    tint = Color.Red, // 图标颜色
                    modifier = Modifier.size(24.dp) // 图标大小
                )
                Text(text = "error")
            }
        } else {
            LazyColumn {
                list.forEach {
                    item { MatchHead2HeadRow(match = it) }
                }
            }
        }
    }

}

@Composable
fun TeamDetailBox(modifier: Modifier, team: Team) {
    Box(
        modifier.wrapContentHeight(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(Modifier.fillMaxWidth()) {
                CrestImage(picUrl = team.crest)
            }
            Text(
                text = team.shortName,
                textAlign = TextAlign.Center,
                modifier = Modifier
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CenterDetailBox(modifier: Modifier, match: Match) {
    Box(modifier) {
        Column(
            Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = convertUtcToChinaDate(match.utcDate),
                fontSize = 15.sp,
                modifier = Modifier.padding(5.dp)
            )
            Text(
                text = convertUtcToChinaTime(match.utcDate),
                fontSize = 15.sp,
                modifier = Modifier.padding(5.dp)
            )
            Text(
                text = if (match.status == Status.FINISHED)
                    "${match.score.fullTime.home} - ${match.score.fullTime.away}"
                else " VS ",
                fontSize = 30.sp,
                modifier = Modifier.padding(5.dp)
            )
            Text(
                fontSize = 15.sp,
                text = if (match.status == Status.FINISHED)
                    "半场${match.score.halfTime.home} - ${match.score.halfTime.away}"
                else match.status?.toChineseString()?:""
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MatchHead2HeadRow(match: Match) {
    Column(
        Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "${match.convertUtcToChinaDate()} ${match.convertUtcToChinaTime()}",
            textAlign = TextAlign.Center,
            modifier = Modifier.background(Color.LightGray)
        )
        Row {
            MatchRow(match = match)  //复用了前面的matchRow
        }
    }
}