package com.example.basic_ui_demo.view.news

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Bitmap
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView


@SuppressLint("SetJavaScriptEnabled")
@Composable
fun NewsWebScreen(url:String?) {

    val context = LocalContext.current
    var backEnable by remember { mutableStateOf(false) }
    var webView: WebView? = null

    Column {
//        TopAppBar(
//            title = {
//
//            },
//            navigationIcon = {
//                IconButton(onClick = { webView?.goBack() }) {
//                    Icon(
//                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
//                        contentDescription = "Back"
//                    )
//                }
//            },
//            actions = {
//                IconButton(onClick = {(context as Activity).finish()}) {
//                    Icon(
//                        imageVector = Icons.Filled.Close,
//                        contentDescription = "close"
//                    )
//                }
//            }
//        )

        AndroidView(
            modifier = Modifier,
            factory = { context ->
                WebView(context).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT,
                    )
                    webViewClient = object : WebViewClient() {
                        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                            backEnable = view!!.canGoBack()
                        }
                    }
                    settings.javaScriptEnabled = true
                    loadUrl(url?:"http://www.bilibili.com")
                    webView = this
                }
            },
            update = {
                webView = it
            }
        )
    }
    BackHandler(enabled = backEnable) {
        if(webView?.canGoBack() == true)
            webView?.goBack()
        else
            (context as Activity).finish()
    }
}