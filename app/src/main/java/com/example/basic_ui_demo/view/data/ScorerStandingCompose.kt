package com.example.basic_ui_demo.view.data

import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
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
import androidx.compose.ui.unit.dp
import com.example.basic_ui_demo.R
import com.example.basic_ui_demo.companion.ApiViewModel
import com.example.basic_ui_demo.companion.RetrofitInstance
import com.example.basic_ui_demo.data_class.scorer.Scorer
import com.example.basic_ui_demo.view.matches.CrestImage
import com.example.footballapidemo.data_class.data.Competition

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun ScorerStandingCompose(competition: Competition, season: MutableState<String>) {
    var isLoading by remember { mutableStateOf(true) }
    var isError by remember { mutableStateOf(false) }
    val scorerList = remember { mutableListOf<Scorer>() }

    LaunchedEffect(season.value) {
        isLoading = true
        val api = RetrofitInstance.api
        val scorerJson = ApiViewModel.callApi {
            api.getCompetitionScorers(competition.code, season.value.toInt())
        }
        isError = scorerJson?.let {
            scorerList.clear()
            scorerList.addAll(it.scorers)
            false
        } ?: true
        isLoading = false
    }

    ScorerHeader()
    if (isError)
        Box(contentAlignment = Alignment.Center) {
            Box(modifier = Modifier.fillMaxSize(0.5f)) {
                Icon(painter = painterResource(id = R.drawable.error_icon), contentDescription = "")
            }
        }
    else if (isLoading)
        Box(contentAlignment = Alignment.Center) {
            CircularProgressIndicator(Modifier.fillMaxSize(0.3f))
        }
    else {
        LazyColumn(Modifier.fillMaxSize()) {
            items(scorerList){
                ScorerRow(scorer = it)
            }
        }
    }
}

@Composable
fun ScorerHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .background(Color(0xFF555555))
            .border(BorderStroke(1.dp, Color.Black)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        DataColumnText(modifier = Modifier.weight(2f), text = "球员", 8.dp)
        DataColumnText(modifier = Modifier.weight(1f), text = "球队", 8.dp)
        DataColumnText(modifier = Modifier.weight(1.5f), text = "进球（点球）", 8.dp)
        DataColumnText(modifier = Modifier.weight(1f), text = "助攻", 8.dp)
    }
}

@Composable
fun ScorerRow(scorer: Scorer) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .background(Color(0xFF555555))
            .border(BorderStroke(1.dp, Color.Black)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        DataColumnText(modifier = Modifier.weight(2f), text = scorer.player.lastName, 8.dp)
        Box(
            Modifier
                .fillMaxHeight(0.9f)
                .weight(1f)
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            CrestImage(picUrl = scorer.team.crest)
        }
        DataColumnText(
            modifier = Modifier.weight(1.5f),
            text = "${scorer.goals}（${scorer.penalties}）",
            8.dp
        )
        DataColumnText(modifier = Modifier.weight(1f), text = "${scorer.assists}", 8.dp)
    }
}