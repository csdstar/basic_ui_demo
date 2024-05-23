package com.example.basic_ui_demo.view.news

import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.basic_ui_demo.NewsActivity
import com.example.basic_ui_demo.companion.ApiViewModel
import com.example.basic_ui_demo.companion.news.NewsRetrofitInstance
import com.example.basic_ui_demo.data_class.news.News
import com.example.basic_ui_demo.view_model.NewsViewModel
import com.lt.compose_views.refresh_layout.RefreshContentStateEnum
import com.lt.compose_views.refresh_layout.RefreshLayoutState
import com.lt.compose_views.refresh_layout.VerticalRefreshableLayout
import com.lt.compose_views.refresh_layout.rememberRefreshLayoutState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

const val refreshNum = 20

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun NewsScreen(viewModel: NewsViewModel) {
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        isLoading = true
        addNewsList(viewModel)
    }

    Row {
        VerticalRefreshableLayout(
            topRefreshLayoutState = rememberRefreshLayoutState(
                onRefreshListener = onRefresh(viewModel)
            ),  //顶部刷新
            bottomRefreshLayoutState = rememberRefreshLayoutState(
                onRefreshListener = onRefresh(viewModel)
            ),  //底部加载
        ) {
            LazyColumn(
                modifier = Modifier,
                userScrollEnabled = true,
            ) {
                items(viewModel.newsList) {
                    NewsRow(it)
                }
            }
        }
    }
}


@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
private fun onRefresh(viewModel: NewsViewModel): RefreshLayoutState.() -> Unit =
    {
        CoroutineScope(Dispatchers.IO).launch {
            addNewsList(viewModel)
            setRefreshState(RefreshContentStateEnum.Stop)  //刷新完成
        }
    }

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
suspend fun addNewsList(viewModel: NewsViewModel) {
    var api = NewsRetrofitInstance.api
    Log.d("MyTag", "refresh")
    var newsJson = ApiViewModel.callApi { api.getNews(1, refreshNum) }
    if (newsJson != null)
        newsJsonCallback(newsJson, viewModel)
    else {
        NewsRetrofitInstance.changeApi()
        api = NewsRetrofitInstance.api
        newsJson = ApiViewModel.callApi { api.getNews(1, refreshNum) }
        if (newsJson != null)
            newsJsonCallback(newsJson, viewModel)
    }
}

@Composable
fun NewsRow(news: News) {
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                border = BorderStroke(width = 1.dp, color = Color.Gray)
            )
            .aspectRatio(9f / 2.7f)  //设置长宽比
            .background(color = Color.LightGray)
            .clickable {
                val intent = Intent(context, NewsActivity::class.java).putExtra("URL", news.url)
                context.startActivity(intent)
            },
        contentAlignment = Alignment.Center

    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .fillMaxHeight()
                .padding(2.dp)
        ) {
            // 左侧部分：标题和描述
            Column(
                modifier = Modifier
                    .weight(3f)
                    .padding(horizontal = 8.dp)
            ) {
                Text(
                    text = news.title,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.weight(3f)
                )
                Text(
                    text = news.ctime,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.weight(1f)
                )
            }
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
            ) {
                NewsImagePainter(picUrl = news.picUrl)
            }
        }
    }
}