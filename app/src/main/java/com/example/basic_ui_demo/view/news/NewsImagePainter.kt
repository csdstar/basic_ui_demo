package com.example.basic_ui_demo.view.news

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import coil.compose.SubcomposeAsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.basic_ui_demo.view.screen.TAG

@Composable
fun NewsImagePainter(
    picUrl: String,
) {
    val context = LocalContext.current

    SubcomposeAsyncImage(
        modifier = Modifier.fillMaxSize(),
        model = ImageRequest
            .Builder(context)
            .data(picUrl)
            .crossfade(false)
            .allowHardware(true)
            .networkCachePolicy(CachePolicy.ENABLED)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .build(),
        contentDescription = picUrl,
        loading = {
            Box(modifier = Modifier.fillMaxSize(0.8f)) {
                CircularProgressIndicator(Modifier.fillMaxSize())
            }
        },
        onError = { Log.e(TAG, "PainterError,url: $picUrl") },
        error = { Icon(imageVector = Icons.Filled.Close, contentDescription = "") },
    )
}
