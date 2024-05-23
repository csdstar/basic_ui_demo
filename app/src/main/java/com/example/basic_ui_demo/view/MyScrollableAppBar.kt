package com.example.basic_ui_demo.view

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.basic_ui_demo.R
import com.lt.compose_views.chain_scrollable_component.ChainScrollableComponent
import com.lt.compose_views.chain_scrollable_component.ChainScrollableComponentState
import com.lt.compose_views.chain_scrollable_component.mode.ChainMode
import com.lt.compose_views.util.ComposePosition
import kotlin.math.roundToInt

/**
 * 可伸缩顶部导航栏
 * Scalable top navigation bar
 *
 * @param title 顶部导航栏标题
 *              Title of top bar
 * @param crest 背景图片
 *                   Background of top bar
 * @param modifier 修饰
 * @param navigationIcon 顶部导航栏图标，默认为返回键
 *                       Icon of top bar
 * @param minScrollPosition 最小滚动位置(距离指定方向的顶点)
 *                          Minimum scroll position
 * @param maxScrollPosition 最大滚动位置(距离指定方向的顶点)
 *                          Maximum scroll position
 * @param onScrollStop 停止滚动时回调,返回true会拦截后续fling操作
 *                     Callback of scroll stop event, return true will intercept subsequent flying operations
 * @param composePosition 设置bar布局所在的位置,并且间接指定了滑动方向
 *                        Set the position of the top bar layout
 * @param chainMode 联动方式
 *                  Chain mode
 * @param content compose内容区域,需要内容是在相应方向可滚动的,并且需要自行给内容设置相应方向的PaddingValues或padding
 *                Content of compose
 */
@ExperimentalFoundationApi
@Composable
fun MyScrollableAppBar(
    title: String,
    crest: String,
    modifier: Modifier = Modifier,
    navigationIcon: @Composable (state: ChainScrollableComponentState) -> Unit =
        remember {
            {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "ArrowBack",
                    tint = Color.White
                )
            }
        },
    minScrollPosition: Dp = 56.dp,
    maxScrollPosition: Dp = 200.dp,
    onScrollStop: ((state: ChainScrollableComponentState, delta: Float) -> Boolean)? = null,
    composePosition: ComposePosition = ComposePosition.Top,
    chainMode: ChainMode = ChainMode.ChainContentFirst,
    content: @Composable BoxScope.(state: ChainScrollableComponentState) -> Unit,
) {
    val density = LocalDensity.current
    // Title 偏移量参考值
    val titleOffsetWidthReferenceValue =
        remember(density) { density.run { navigationIconSize.roundToPx().toFloat() } }
    ChainScrollableComponent(
        modifier = modifier,
        minScrollPosition = minScrollPosition,
        maxScrollPosition = maxScrollPosition,
        onScrollStop = onScrollStop,
        composePosition = composePosition,
        chainMode = chainMode,
        chainContent = { state ->
            Box(
                modifier = Modifier
                    .height(maxScrollPosition)
                    .fillMaxWidth()
                    .offset {
                        IntOffset(
                            0,
                            state
                                .getScrollPositionValue()
                                .roundToInt()
                        )
                    }
            ) {
                Image(
                    painter = painterResource(id = R.drawable.playground),
                    contentDescription = "background",
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier.fillMaxSize()
                )
                Box(
                    modifier = Modifier.height(maxScrollPosition/2),
                ){
                    CrestImage(picUrl = crest)
                }
                // 导航图标
                Box(
                    modifier = Modifier
                        .width(navigationIconSize)
                        .height(minScrollPosition)
                        .offset {
                            IntOffset(
                                x = 0,
                                y = -state
                                    .getScrollPositionValue()
                                    .roundToInt() //保证应用栏是始终不动的
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    navigationIcon(state)
                }
                Box(
                    modifier = Modifier
                        .height(minScrollPosition) //和ToolBar同高
                        .align(Alignment.BottomStart)
                        .offset {
                            IntOffset(
                                x = (state.getScrollPositionPercentage() * titleOffsetWidthReferenceValue).roundToInt(),
                                y = 0
                            )
                        },
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        text = title,
                        color = Color.White,
                        modifier = Modifier.padding(start = 20.dp),
                        fontSize = 20.sp
                    )
                }
            }
        }, content = content
    )
}

// 导航图标大小
private val navigationIconSize = 50.dp