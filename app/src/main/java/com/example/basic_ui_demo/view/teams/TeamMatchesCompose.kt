package com.example.basic_ui_demo.view.teams

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.basic_ui_demo.R
import com.example.basic_ui_demo.companion.ApiViewModel
import com.example.basic_ui_demo.companion.convertDateStringToYearAndMonth
import com.example.basic_ui_demo.companion.convertUtcToChinaCertainDate
import com.example.basic_ui_demo.companion.convertUtcToChinaTime
import com.example.basic_ui_demo.companion.data.RetrofitInstance
import com.example.basic_ui_demo.data_class.data.Status
import com.example.basic_ui_demo.view.CrestImage
import com.example.basic_ui_demo.view.screen.TAG
import com.example.basic_ui_demo.view_model.TeamsViewModel
import com.example.footballapidemo.data_class.data.Match
import com.example.footballapidemo.data_class.data.MatchesJson

@RequiresApi(Build.VERSION_CODES.O)
@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun TeamMatchesCompose(viewModel: TeamsViewModel, teamId: Int) {
    var isLoading by remember { mutableStateOf(true) }
    //加载指示器

    var isError by remember { mutableStateOf(false) }
    //error指示器

    var curSeason by viewModel.season
    //当前赛季

    var matchesJson: MatchesJson?


    val matchList = remember { mutableListOf<Match>() }

    val matchGroups = remember { mutableMapOf<String, List<Match>>() }
    LaunchedEffect(Unit) {
        isLoading = true
        val api = RetrofitInstance.api
        matchesJson = ApiViewModel.callApi {
            api.getMatchesByTeamId(teamId = teamId, season = curSeason)
        }
        isError = matchesJson?.let {
            matchList.clear()
            matchList.addAll(it.matches)
            matchList.sortBy { match ->
                convertUtcToChinaTime(match.utcDate)
            }
            matchGroups.putAll(
                matchList.groupBy { match ->
                    val date = convertUtcToChinaCertainDate(match.utcDate)
                    val yearMonth = convertDateStringToYearAndMonth(date)
                    yearMonth
                }
            )
            Log.d(TAG, "TeamMatchCompose,matchGroups = $matchGroups")
            false
        } ?: true
        isLoading = false
    }

    if (isLoading) {
        Box(
            modifier = Modifier.padding(top = 120.dp),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(Modifier.fillMaxSize(0.5f))
        }
    } else if(isError){
        Box(
            modifier = Modifier.padding(top = 120.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(painterResource(id = R.drawable.error_icon),"")
        }
    } else {
        LazyColumn(
            contentPadding = PaddingValues(top = 120.dp)
        ) {
            item { SeasonHeader(curSeason) { curSeason = it } }
            matchGroups.forEach { (yearMonth, matches) ->
                item { YearMonthRow(yearMonth) }
                items(matches) {
                    TeamMatchRow(match = it, teamId = teamId)
                }
            }
        }
    }
}

@Composable
fun YearMonthRow(yearMonth: String) {
    val parts = yearMonth.split("-")
    val year = parts[0]
    val month = parts[1]

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.LightGray),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text("$year 年$month 月")
    }
}

//单场比赛的样式
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TeamMatchRow(match: Match, teamId: Int) {
    Row(
        Modifier
            .fillMaxWidth()
            .height(60.dp)
    ) {
        Row(
            Modifier
                .width(150.dp)
                .padding(5.dp)
                .fillMaxHeight(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(Modifier.padding(4.dp)) {
                Text(
                    text = match.getMatchTime(),
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = match.getMatchTitle(
                        TeamsViewModel.competitions,
                        TeamsViewModel.competitionsCode
                    ),
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        Row(
            modifier = Modifier
                .padding(15.dp)
                .fillMaxHeight()
                .weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            val home = match.homeTeam
            val away = match.awayTeam

            //homeTeam
            Text(
                modifier = Modifier.weight(0.3f),
                text = home.shortName,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.End
            )
            Box(
                modifier = Modifier.fillMaxHeight(0.8f),
                contentAlignment = Alignment.Center
            ) {
                CrestImage(picUrl = home.crest)
            }

            //中间比分
            if (match.status == Status.FINISHED) {
                Text(
                    text = "${match.score.fullTime.home}-${match.score.fullTime.away}",
                    color = Color.White,
                    style = TextStyle(fontWeight = FontWeight.Bold),
                    modifier = Modifier
                        .padding(horizontal = 10.dp)
                        .background(
                            teamWinColor(match, teamId),
                            shape = RoundedCornerShape(6.dp)
                        ),
                    maxLines = 1
                )
            } else {
                Text(
                    text = "VS",
                    style = TextStyle(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(horizontal = 13.dp)
                )
            }

            //awayTeam
            Box(
                modifier = Modifier.fillMaxHeight(0.8f),
                contentAlignment = Alignment.Center
            ) {
                CrestImage(picUrl = away.crest)
            }
            Text(
                modifier = Modifier.weight(0.3f),
                text = away.shortName,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

fun teamWinColor(match: Match, teamId: Int): Color {
    return (
            if (match.homeTeam.id == teamId)
                when (match.score.winner) {
                    "HOME_TEAM" -> Color.Red
                    "AWAY_TEAM" -> Color.Blue
                    else -> Color.Green
                }
            else
                when (match.score.winner) {
                    "HOME_TEAM" -> Color.Blue
                    "AWAY_TEAM" -> Color.Red
                    else -> Color.Green
                }
            ).copy(0.7f)
}