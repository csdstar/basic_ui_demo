package com.example.basic_ui_demo.view.data

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DataColumnText(
    modifier: Modifier = Modifier,
    text: String,
    paddingValues: Dp = 3.dp
) {
    Text(
        modifier = modifier.padding(paddingValues),
        text = text,
        textAlign = TextAlign.Center,
        fontSize = 12.sp,
        color = Color.White,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
fun DataColumnLeftAlignedText(modifier: Modifier, text: String) {
    Text(
        text = text,
        modifier = modifier.padding(8.dp),
        textAlign = TextAlign.Left,
        fontSize = 12.sp,
        color = Color.White,
        overflow = TextOverflow.Ellipsis
    )
}