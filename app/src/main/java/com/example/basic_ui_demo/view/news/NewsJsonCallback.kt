package com.example.basic_ui_demo.view.news

import android.util.Log
import com.example.basic_ui_demo.data_class.news.NewsJson
import com.example.basic_ui_demo.view.screen.TAG
import com.example.basic_ui_demo.view_model.NewsViewModel

fun newsJsonCallback(newsJson: NewsJson, newsViewModel: NewsViewModel){
    Log.d(TAG, newsJson.msg)
    val newsList = newsJson.result?.newslist
    if (newsList != null){
        for(news in newsList){
            news.picUrl = changeUrl(news.picUrl)
            newsViewModel.addNews(news)
            Log.d("MyTag", "title: ${news.title},picUrl: ${news.picUrl}")
        }
    }
    else{
        Log.e(TAG, "no newsList")
    }
}

fun changeUrl(url: String): String {
    var outUrl = url
    if (!url.startsWith("https://") && !url.startsWith("http://")) {
        outUrl = if (url.startsWith("//")) {
            "https:$url"
        } else if (url.startsWith("http://")) {
            outUrl.replace("http://", "https://")
        } else {
            "https://$url"
        }
    }
    return outUrl
}