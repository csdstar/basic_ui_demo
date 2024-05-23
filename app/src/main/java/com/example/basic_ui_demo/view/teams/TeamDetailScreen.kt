package com.example.basic_ui_demo.view.teams

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.basic_ui_demo.R
import com.example.basic_ui_demo.companion.ApiViewModel
import com.example.basic_ui_demo.companion.data.RetrofitInstance
import com.example.basic_ui_demo.view.MyScrollableAppBar
import com.example.basic_ui_demo.view_model.TeamsViewModel
import com.example.footballapidemo.data_class.data.MatchesJson
import com.lt.compose_views.compose_pager.ComposePager
import com.lt.compose_views.compose_pager.rememberComposePagerState
import com.lt.compose_views.pager_indicator.TextPagerIndicator

@OptIn(ExperimentalFoundationApi::class)
@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TeamDetailScreen(viewModel: TeamsViewModel, teamId: Int) {
    val tabs = listOf("基本信息", "赛程", "阵容")
    //供选择的tabs

    val composePagerState = rememberComposePagerState()
    //创建pagerState

    val curIndex by remember { composePagerState.getCurrSelectIndexState() }
    //pager当前index(对应tabs)

    var isLoading by remember { viewModel.isLoading }
    //加载指示器

    var isError by remember { viewModel.isError }
    //加载失败指示器

    val curSeason by remember { viewModel.season }
    //当前选择的赛季

    var team by remember { mutableStateOf(viewModel.getTeam(curSeason, teamId)) }
    //当前加载的team

    var matchesJson by remember { mutableStateOf<MatchesJson?>(null) }

    if (teamId == 0) {
        isLoading = false
        isError = true
    } else {
        team = viewModel.getTeam(curSeason, teamId)
    }

    LaunchedEffect(curIndex, curSeason) {
        isLoading = true
        val api = RetrofitInstance.api
        if (team == null) {
            team = ApiViewModel.callApi {
                api.getTeamById(teamId, curSeason)
            }
        }
        isLoading = false
    }

    Column {
        TextPagerIndicator(
            texts = tabs,
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
        ComposePager(
            pageCount = tabs.size,
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
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(Modifier.fillMaxSize(0.3f))
                }
            else {
                //可伸展的topBar
                MyScrollableAppBar(
                    title = team?.shortName.toString(),
                    crest = team?.crest.toString(),
                    maxScrollPosition = 120.dp,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                ) {
                    when (tabs[curIndex]) {
                        "基本信息" -> {
                            InformationCompose(viewModel)
                        }

                        "阵容" -> {
                            SquadCompose()
                        }

                        "赛程" -> {
                            TeamMatchesCompose(viewModel,teamId)
                        }
                    }
                }
            }
        }
    }
}
