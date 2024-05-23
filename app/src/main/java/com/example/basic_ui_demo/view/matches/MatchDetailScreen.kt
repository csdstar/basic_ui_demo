package com.example.basic_ui_demo.view.matches

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.basic_ui_demo.DetailScreenColor
import com.example.basic_ui_demo.companion.ApiViewModel
import com.example.basic_ui_demo.companion.data.RetrofitInstance
import com.example.basic_ui_demo.companion.convertUtcToChinaCertainDate
import com.example.basic_ui_demo.companion.convertUtcToChinaTime
import com.example.basic_ui_demo.data_class.data.Aggregates
import com.example.basic_ui_demo.data_class.data.Status
import com.example.basic_ui_demo.view.screen.TAG
import com.example.basic_ui_demo.view.CrestImage
import com.example.footballapidemo.data_class.data.Match
import com.example.footballapidemo.data_class.data.Team
import com.example.basic_ui_demo.view_model.MatchesViewModel
import java.text.DecimalFormat

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MatchDetailScreen() {
    val match = MatchesViewModel.match
    var isLoading by remember { mutableStateOf(false) }
    var isError by remember { mutableStateOf(false) }
    val list = remember { mutableStateListOf<Match>() }
    var aggregates by remember { mutableStateOf<Aggregates?>(null) }

    LaunchedEffect(Unit) {
        //加载历史交手记录
        isLoading = true
        val api = RetrofitInstance.api
        val head2headJson = ApiViewModel.callApi {
            api.getHead2Head(match.id)
        }
        if (head2headJson == null)
            isError = true
        else {
            Log.d(TAG, head2headJson.aggregates.toString())
            aggregates = (head2headJson.aggregates)
            list.addAll(head2headJson.matches)
            list.sortBy { it.convertUtcToChinaLocalDate() }
        }
        isLoading = false
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        //顶部界面，两侧是球队图标，中间为比赛信息
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(DetailScreenColor),
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
            text = "近期比赛",
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Gray),
            textAlign = TextAlign.Center,
            fontSize = 20.sp,
            color = Color.White
        )
        if (isLoading)
            CircularProgressIndicator()
        else if (isError) {
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
            //近期比赛列表
            LazyColumn(Modifier.fillMaxWidth()) {
                list.forEach {
                    item { MatchHead2HeadRow(match = it) }
                }
                item { HorizontalDivider(thickness = 2.dp, color = Color.Black) }
            }
            Spacer(modifier = Modifier.height(30.dp))

            //历史统计
            Text(
                text = "历史统计",
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Gray),
                textAlign = TextAlign.Center,
                fontSize = 20.sp,
                color = Color.White
            )
            HorizontalDivider(thickness = 1.dp, color = Color.Black)
            TotalHead2HeadBar()
            TotalHead2HeadData(match, aggregates, true)
            TotalHead2HeadData(match, aggregates, false)
        }
    }
}

//球队图标和名字
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
                modifier = Modifier.padding(15.dp),
                color = Color.White,
                fontSize = 20.sp,
                maxLines = 1, // 设置最大行数为1
                overflow = TextOverflow.Ellipsis // 设置文本溢出时显示省略号
            )
        }
    }
}

//显示比赛信息
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CenterDetailBox(modifier: Modifier, match: Match) {
    Box(modifier) {
        Column(
            Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = convertUtcToChinaCertainDate(match.utcDate),
                fontSize = 15.sp,
                modifier = Modifier.padding(5.dp),
                color = Color.White,
            )
            Text(
                text = convertUtcToChinaTime(match.utcDate),
                fontSize = 15.sp,
                modifier = Modifier.padding(5.dp),
                color = Color.White,
            )
            Text(
                text = if (match.status == Status.FINISHED)
                    "${match.score.fullTime.home} - ${match.score.fullTime.away}"
                else " VS ",
                fontSize = 30.sp,
                modifier = Modifier.padding(5.dp),
                color = Color.White,
            )
            Text(
                fontSize = 15.sp,
                color = Color.White,
                text = if (match.status == Status.FINISHED)
                    "半场${match.score.halfTime.home} - ${match.score.halfTime.away}"
                else match.status?.toChineseString() ?: ""
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
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.LightGray)
        )
        Row {
            MatchRow(match = match)  //复用了前面的matchRow
        }
    }
}

@Composable
fun TotalHead2HeadBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.Gray), // 设置表头的背景颜色
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "球队",
            modifier = Modifier
                .weight(1f)  // 使用 weight 分配空间
                .padding(8.dp),  //padding
            color = Color.White, // 设置文本颜色
            textAlign = TextAlign.Start // 设置左对齐
        )
        Text(
            text = "赛",
            modifier = Modifier
                .padding(12.dp),
            color = Color.White, // 设置文本颜色
            textAlign = TextAlign.End // 设置右对齐
        )
        Text(
            text = "胜",
            modifier = Modifier
                .padding(8.dp),
            color = Color.White, // 设置文本颜色
            textAlign = TextAlign.End // 设置右对齐
        )
        Text(
            text = "平",
            modifier = Modifier
                .padding(8.dp),
            color = Color.White, // 设置文本颜色
            textAlign = TextAlign.End // 设置右对齐
        )
        Text(
            text = "负",
            modifier = Modifier
                .padding(8.dp),
            color = Color.White, // 设置文本颜色
            textAlign = TextAlign.End // 设置右对齐
        )
        Text(
            text = "胜率",
            modifier = Modifier
                .padding(15.dp),
            color = Color.White, // 设置文本颜色
            textAlign = TextAlign.End // 设置右对齐
        )
    }
}

//历史统计数据栏
@Composable
fun TotalHead2HeadData(match: Match, aggregates: Aggregates?, isHome: Boolean) {
    if (aggregates == null) {  //空对象检查
        Log.d(TAG, "null aggregates")
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Box(
                modifier = Modifier
                    .height(20.dp)
                    .weight(1f) // 使用 weight 分配空间
                    .padding(8.dp), // 设置内边距
            ) {
                CrestImage(
                    picUrl =
                    if (isHome) match.homeTeam.crest
                    else match.awayTeam.crest
                )
            }
            Text(
                text = "null",
                modifier = Modifier
                    .padding(15.dp),
                color = Color.White, // 设置文本颜色
                textAlign = TextAlign.End // 设置右对齐
            )
        }
    } else {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Box(
                modifier = Modifier
                    .weight(1f) // 使用 weight 分配全部剩余空间，使得Box的内容在左侧
                    .height(40.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                CrestImage(
                    picUrl =
                    if (isHome) match.homeTeam.crest
                    else match.awayTeam.crest
                )
            }
            //总场次
            Text(
                text = aggregates.numberOfMatches.toString(),
                modifier = Modifier
                    .padding(12.dp),
                textAlign = TextAlign.End // 设置右对齐
            )
            //胜
            Text(
                text = aggregates.returnTeam(isHome).wins.toString(),
                //这里用的是在数据类Aggregates中定义的函数，便于返回homeTeam和awayTeam
                modifier = Modifier
                    .padding(8.dp),
                textAlign = TextAlign.End // 设置右对齐
            )
            //平
            Text(
                text = aggregates.returnTeam(isHome).draws.toString(),
                modifier = Modifier
                    .padding(8.dp),
                textAlign = TextAlign.End // 设置右对齐
            )
            //负
            Text(
                text = aggregates.returnTeam(isHome).losses.toString(),
                modifier = Modifier
                    .padding(8.dp),
                textAlign = TextAlign.End // 设置右对齐
            )
            //胜率
            Text(
                text = (aggregates.returnTeam(isHome).wins.toDouble() / aggregates.numberOfMatches.toDouble()).toPercentageString(),
                modifier = Modifier
                    .padding(15.dp),
                textAlign = TextAlign.End // 设置右对齐
            )
        }
        HorizontalDivider(thickness = 1.dp, color = Color.Black)
    }
}

fun Double.toPercentageString(): String {
    val percentage = this * 100
    val decimalFormat = DecimalFormat("#0.00")
    return "${decimalFormat.format(percentage)}%"
}