package com.example.basic_ui_demo.view.matches

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import coil.compose.SubcomposeAsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.basic_ui_demo.R
import com.example.basic_ui_demo.screen.TAG

@Composable
fun CrestImage(picUrl: String?) {  //自动fillMaxHeight并设定长宽比
    val context = LocalContext.current
    val mutUrl by remember { mutableStateOf(picUrl) }
    if (mutUrl == null) {
        Image(
            painter = painterResource(id = R.drawable.error_icon),
            contentDescription = "",
            modifier = Modifier
                .aspectRatio(1f / 1f)
                .fillMaxHeight()
        )
    } else {
        SubcomposeAsyncImage(
            modifier = Modifier
                .aspectRatio(1f / 1f)
                .fillMaxHeight(),
            model = ImageRequest
                .Builder(context)
                .data(mutUrl)
                .crossfade(false)
                .allowHardware(true)
                .networkCachePolicy(CachePolicy.ENABLED)
                .memoryCachePolicy(CachePolicy.ENABLED)
                .build(),
            contentDescription = picUrl,
            contentScale = ContentScale.FillBounds,
            onError = { Log.e(TAG, "PainterError,url: $picUrl") },
            loading = {
                Box(modifier = Modifier.fillMaxSize(0.8f)) {
                    CircularProgressIndicator(Modifier.fillMaxSize())
                }
            },
            error = {
                Icon(
                    painter = painterResource(id = R.drawable.error_icon),
                    contentDescription = "",
                    Modifier
                        .aspectRatio(1f / 1f)
                        .fillMaxHeight()
//                        .clickable {
//                            mutUrl = null
//                            mutUrl = picUrl
//                        }
                )
            }
        )
    }
}