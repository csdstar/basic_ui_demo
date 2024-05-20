package com.example.basic_ui_demo.view.data

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.basic_ui_demo.R
import com.example.basic_ui_demo.companion.ApiViewModel
import com.example.basic_ui_demo.companion.RetrofitInstance
import com.example.basic_ui_demo.screen.TAG
import com.example.basic_ui_demo.view.matches.CrestImage
import com.example.footballapidemo.data_class.data.Competition
import com.example.footballapidemo.data_class.data.Table

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun TeamStandingCompose(competition: Competition, season: MutableState<String>) {
    var isLoading by remember { mutableStateOf(true) }
    var isError by remember { mutableStateOf(false) }
    val tableList = remember { mutableListOf<Table>() }

    LaunchedEffect(season.value) {
        isLoading = true
        val api = RetrofitInstance.api
        val standingsJson = ApiViewModel.callApi {
            api.getCompetitionStandings(competition.code, season.value.toInt())
        }
        isError = standingsJson?.let {
            tableList.clear()
            tableList.addAll(it.standings[0].table)
            Log.d(TAG, tableList[0].toString())
            false
        } ?: true
        isLoading = false
    }

    CompetitionTypeHeader(type = competition.type)
    StandingDataHeader()

    if (isError)
        Box(contentAlignment = Alignment.Center){
            Box(modifier = Modifier.fillMaxSize(0.5f)){
                Image(painter = painterResource(id = R.drawable.error_icon), contentDescription = "")
            }
        }
    else if (isLoading)
        Box(contentAlignment = Alignment.Center){
            CircularProgressIndicator(Modifier.fillMaxSize(0.3f))
        }
    else {
        LazyColumn(Modifier.fillMaxSize()) {
            items(tableList){
                TableDataRow(it)
            }
        }
    }

}

@Composable
fun StandingDataHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .background(Color(0xFF555555)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        DataColumnText(Modifier.weight(1f), "球队")
        DataColumnText(Modifier.width(30.dp), "赛")
        DataColumnText(Modifier.width(30.dp), "胜")
        DataColumnText(Modifier.width(30.dp), "平")
        DataColumnText(Modifier.width(30.dp), "负")
        DataColumnText(Modifier.width(50.dp), "进/失")
        DataColumnText(Modifier.width(30.dp), "积分")
    }
}

@Composable
fun CompetitionTypeHeader(type: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .background(Color(0xFF313131)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = type,
            textAlign = TextAlign.Center,
            fontSize = 14.sp,
            color = Color.White
        )
    }
}

@Composable
fun TableDataRow(table: Table) {
    Row(
        Modifier
            .fillMaxWidth()
            .height(40.dp)
            .background(Color(0xFF313131)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        DataColumnLeftAlignedText(
            modifier = Modifier.weight(1.2f),
            text = "${table.position}  ${table.team.shortName}"
        )
        Box(
            Modifier
                .fillMaxHeight(0.8f)
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            CrestImage(picUrl = table.team.crest)
        }
        DataColumnText(Modifier.width(30.dp),text = table.playedGames.toString())
        DataColumnText(Modifier.width(30.dp),text = table.won.toString())
        DataColumnText(Modifier.width(30.dp),text = table.draw.toString())
        DataColumnText(Modifier.width(30.dp),text = table.lost.toString())
        DataColumnText(Modifier.width(50.dp),text = "${table.goalsFor}/${table.goalsAgainst}")
        DataColumnText(Modifier.width(30.dp),text = table.points.toString())
    }
}

@Composable
@Preview(showBackground = true)
fun PreviewStandingTableHeader() {
    StandingDataHeader()
}