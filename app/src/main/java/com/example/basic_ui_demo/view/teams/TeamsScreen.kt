@file:RequiresApi(Build.VERSION_CODES.O)

package com.example.basic_ui_demo.view.teams

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.basic_ui_demo.R
import com.example.basic_ui_demo.companion.ApiViewModel
import com.example.basic_ui_demo.companion.data.RetrofitInstance
import com.example.basic_ui_demo.data_class.teams.Team
import com.example.basic_ui_demo.view.CrestImage
import com.example.basic_ui_demo.view.screen.Screen
import com.example.basic_ui_demo.view_model.TeamsViewModel
import com.lt.compose_views.compose_pager.ComposePager
import com.lt.compose_views.compose_pager.rememberComposePagerState
import com.lt.compose_views.pager_indicator.TextPagerIndicator


@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun TeamsScreen(navController: NavController, viewModel: TeamsViewModel) {

    val competitions = TeamsViewModel.competitions
    val competitionsCode = TeamsViewModel.competitionsCode
    //获取viewModel中的静态成员

    val composePagerState = rememberComposePagerState()
    //创建pagerState

    val curIndex by remember { composePagerState.getCurrSelectIndexState() }
    //pager当前index(对应competitions)

    var curSeason by remember { mutableStateOf("2023") }
    //当前选择的赛季

    var isLoading by remember { viewModel.isLoading }
    //加载指示器

    var isError by remember { viewModel.isError }
    //加载失败指示器

    val teams = remember { mutableListOf<Team>() }


    LaunchedEffect(curIndex, curSeason) {
        isLoading = true
        viewModel.setIndex(curIndex)
        var teamsJson = viewModel.getPageData(curSeason)
        if (teamsJson == null) {
            val api = RetrofitInstance.api
            teamsJson = ApiViewModel.callApi {
                api.getCompetitionTeams(competitionsCode[curIndex], curSeason)
            }
        }
        isError = teamsJson?.let {
            viewModel.setPageData(it, curSeason)
            it.teams.forEach { team ->
                viewModel.addTeam(curSeason, team.id, team)
            }
            teams.clear()
            teams.addAll(it.teams)
            false
        } ?: true
        isLoading = false
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

        SeasonHeader { curSeason = it }
        //赛季选择器

        ComposePager(
            pageCount = competitions.size,
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.CenterHorizontally),
            composePagerState = composePagerState,
            orientation = Orientation.Horizontal,
            pageCache = 1,
        ) {
            if (isError)
                Box(contentAlignment = Alignment.Center) {
                    Box(modifier = Modifier.fillMaxSize(0.5f)) {
                        Image(
                            painter = painterResource(id = R.drawable.error_icon),
                            contentDescription = ""
                        )
                    }
                }
            else if (isLoading)
                Box(contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(Modifier.fillMaxSize(0.3f))
                }
            else {
                TeamsCompose(navController = navController, teams = teams)
            }
        }
    }
}

@Composable
fun TeamCrestBox(modifier: Modifier, name: String, picUrl: String) {
    Box(
        modifier.height(100.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(Modifier.fillMaxHeight(0.6f)) {
                CrestImage(picUrl = picUrl)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = name, textAlign = TextAlign.Center)
        }
    }
}

@Composable
fun TeamsCompose(navController: NavController, teams: List<Team>) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 100.dp),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(10.dp)
    ) {
        items(teams) {
            TeamCrestBox(
                modifier = Modifier.clickable {
                    navController.navigate(Screen.TeamDetailScreen.createRoute(it.id))
                },
                name = it.shortName, picUrl = it.crest
            )
        }
    }
}