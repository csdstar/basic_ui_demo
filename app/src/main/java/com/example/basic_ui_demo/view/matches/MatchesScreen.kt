package com.example.basic_ui_demo.view.matches

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.basic_ui_demo.companion.ApiViewModel
import com.example.basic_ui_demo.companion.convertUtcToChinaTime
import com.example.basic_ui_demo.companion.getCurrentDateString
import com.example.basic_ui_demo.companion.getDateStringWithOffset
import com.example.basic_ui_demo.data_class.data.Status
import com.example.basic_ui_demo.view.screen.Screen
import com.example.basic_ui_demo.view.screen.TAG
import com.example.basic_ui_demo.view.CrestImage
import com.example.footballapidemo.data_class.data.Match
import com.example.footballapidemo.data_class.data.Team
import com.example.footballapidemo.view.matches.SearchCompose
import com.example.basic_ui_demo.view_model.MatchesViewModel
import com.example.basic_ui_demo.view_model.MatchesViewModel.Companion.competitionsCode
import com.example.basic_ui_demo.view_model.MatchesViewModel.Companion.getMatchTitle
import com.lt.compose_views.compose_pager.ComposePager
import com.lt.compose_views.compose_pager.rememberComposePagerState
import com.lt.compose_views.pager_indicator.TextPagerIndicator
import com.lt.compose_views.refresh_layout.RefreshContentStateEnum
import com.lt.compose_views.refresh_layout.RefreshLayoutState
import com.lt.compose_views.refresh_layout.VerticalRefreshableLayout
import com.lt.compose_views.refresh_layout.rememberRefreshLayoutState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import java.time.LocalDate

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MatchesScreen(navController: NavController, viewModel: MatchesViewModel) {
    val competitions = MatchesViewModel.competitions
    //联赛名

    val composePagerState = rememberComposePagerState()
    //pagerState

    val curIndex by composePagerState.getCurrSelectIndexState()
    //pager的当前index

    val matchGroups = remember(curIndex) { viewModel.pagesData[curIndex].matchGroups }
    //当前页面的matchGroup

    val isLoading by viewModel.isLoading

    val isSearching by viewModel.isSearching

    val apiMessage by ApiViewModel.message

    //弹出查询框
    if (isSearching)
        SearchCompose(viewModel)

    //整个screen的初始化加载,在后台加载除第一页之外的其他页面数据
    LaunchedEffect(Unit) {
        competitionsCode.forEachIndexed{ index,code ->
            val dateFrom = viewModel.pagesData[index].dateFrom.value
            val dateTo = viewModel.pagesData[index].dateTo.value
            when(index){
                0 -> {viewModel.initDone(0)}
                else ->{ initLoadingPage(code, dateFrom, dateTo, index, viewModel) }
            }
        }
    }

    Box(Modifier.fillMaxSize()) {
        Column(Modifier.fillMaxSize()) {

            LaunchedEffect(curIndex) {
                pageInitialize(viewModel, curIndex)
            }

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

            ComposePager(
                pageCount = competitions.size,
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.CenterHorizontally),
                composePagerState = composePagerState,
                orientation = Orientation.Horizontal,
                pageCache = 1,
            ) {
                Box(Modifier.fillMaxSize()) {
                    if (isLoading) {
                        Column(
                            Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = apiMessage,
                                fontSize = 20.sp
                            )
                            Spacer(modifier = Modifier.height(15.dp))
                            CircularProgressIndicator()
                        }
                    } else {
                        VerticalRefreshableLayout(
                            topRefreshLayoutState = rememberRefreshLayoutState(
                                onRefreshListener = topRefresh(viewModel)
                            ),  //顶部刷新
                            bottomRefreshLayoutState = rememberRefreshLayoutState(
                                onRefreshListener = bottomRefresh(viewModel)
                            ),  //底部加载
                        ) {
                            Column {
                                Spacer(modifier = Modifier.height(20.dp))
                                MatchGroup(navController, matchGroups)
                            }
                        }
                    }
                    //悬浮查找按钮
                    FloatingButtonCompose(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(bottom = 30.dp),
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun MatchRow(
    modifier: Modifier = Modifier,
    navController: NavController,
    match: Match
) {  //matchRow的重载形式，添加了导航功能，用于导航到比赛详情页
    MatchRow(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                MatchesViewModel.match = match  //在viewModel中存储被点击的match
                navController.navigate(Screen.MatchDetailScreen.route)
            },
        match = match
    )
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
//单个比赛的样式
fun MatchRow(modifier: Modifier = Modifier, match: Match) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 左边球队信息
        LeftTeamBox(modifier = Modifier.weight(1f), team = match.homeTeam)

        // 中间数据
        Box(
            modifier = Modifier
                .weight(0.7f)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = getMatchTitle(match),
                    fontSize = 10.sp,
                    style = TextStyle(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(horizontal = 6.dp)
                )

                if (match.status == Status.FINISHED) {
                    Text(
                        text = "${match.score.fullTime.home}-${match.score.fullTime.away}",
                        style = TextStyle(fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(horizontal = 16.dp),
                        maxLines = 1
                    )
                } else {
                    Text(
                        text = "VS",
                        style = TextStyle(fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
                Text(
                    text = convertUtcToChinaTime(match.utcDate),
                    fontSize = 10.sp,
                    style = TextStyle(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(horizontal = 6.dp)
                )
            }
        }

        // 右边球队信息
        RightTeamBox(modifier = Modifier.weight(1f), team = match.awayTeam)
    }
}



@Composable
fun LeftTeamBox(modifier: Modifier, team: Team) {
    Box(
        modifier = modifier
            .fillMaxHeight(),
    ) {
        Row(
            modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier
                    .padding(10.dp)
                    .weight(1f),
                text = team.shortName,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.End
            )
            Box(
                modifier = Modifier.width(50.dp)
            ) {
                CrestImage(picUrl = team.crest)
            }
        }
    }
}

@Composable
fun RightTeamBox(modifier: Modifier, team: Team) {
    Box(
        modifier = modifier
            .fillMaxHeight(),
    ) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.width(50.dp)
            ) {
                CrestImage(picUrl = team.crest)
            }
            Text(
                modifier = Modifier.padding(10.dp),
                text = team.shortName,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun MatchGroup(
    navController: NavController,
    matchGroups: Map<LocalDate, SnapshotStateList<Match>>
) {
    //分日期显示比赛列表
    if (matchGroups.isEmpty()) {
        val date = getCurrentDateString()
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
        ) {
            item {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .height(30.dp)
                        .background(Color.LightGray),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) { Text(text = date) }
            }

            item {
                Box(contentAlignment = Alignment.Center) {
                    Text(text = "查询无结果，请尝试寻找其他时间的比赛", textAlign = TextAlign.Center)
                }
            }
        }
    } else {
        val sortedMatchGroups = matchGroups.toList().sortedBy { (date, _) -> date }
        //将条目按日期排序

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
        ) {
            sortedMatchGroups.forEach { (date, matches) ->
                item {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .height(30.dp)
                            .background(Color.LightGray),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = date.toString())
                    }
                }
                items(matches) { match ->
                    MatchRow(navController = navController, match = match)
                    Spacer(modifier = Modifier.height(5.dp))
                }
            }
        }
    }
}

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@RequiresApi(Build.VERSION_CODES.O)
fun topRefresh(  //顶部刷新
    viewModel: MatchesViewModel
): (RefreshLayoutState.() -> Unit) = {
    CoroutineScope(Dispatchers.IO).launch {
        val index = viewModel.index
        val code = competitionsCode[index]
        val days: Long = if (index == 0) 3 else 15
        var dateFrom by viewModel.pagesData[index].dateFrom
        val newDateFrom = getDateStringWithOffset(dateFrom, days, false)
        Log.d(TAG, "$dateFrom  $newDateFrom")

        addMatchesByCompetitionCode(code, newDateFrom, dateFrom, viewModel)
        dateFrom = newDateFrom
        setRefreshState(RefreshContentStateEnum.Stop)
    }
}

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@RequiresApi(Build.VERSION_CODES.O)
fun bottomRefresh(  //底部加载
    viewModel: MatchesViewModel
): (RefreshLayoutState.() -> Unit) = {
    CoroutineScope(Dispatchers.IO).launch {
        val index = viewModel.index
        val code = competitionsCode[index]
        val days: Long = if (index == 0) 3 else 15
        var dateTo by viewModel.pagesData[index].dateTo
        val newDateTo = getDateStringWithOffset(dateTo, days, true)
        Log.d(TAG, "$dateTo  $newDateTo")

        addMatchesByCompetitionCode(code, dateTo, newDateTo, viewModel)
        dateTo = newDateTo
        setRefreshState(RefreshContentStateEnum.Stop)
    }
}

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@RequiresApi(Build.VERSION_CODES.O)
suspend fun pageInitialize(viewModel: MatchesViewModel, curIndex: Int) {
    //每次切换页面时进行的初始化加载

    var isLoading by viewModel.isLoading
    var isSearching by viewModel.isSearching

    isSearching = false
    isLoading = true
    viewModel.setIndex(curIndex)
    val code = competitionsCode[curIndex]
    val dateFrom = viewModel.pagesData[curIndex].dateFrom.value
    val dateTo = viewModel.pagesData[curIndex].dateTo.value
    Log.d(TAG, "curIndex:$curIndex")

    viewModel.pagesData[curIndex].initLoading.filter { it }.collect{
        if (viewModel.pagesData[curIndex].matchGroups.isEmpty()){
            addMatchesByCompetitionCode(code, dateFrom, dateTo, viewModel)
        }
        isLoading = false
    }
}