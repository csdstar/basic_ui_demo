package com.example.basic_ui_demo.view.teams

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.basic_ui_demo.view_model.TeamsViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun InformationCompose(viewModel:TeamsViewModel){
    var curSeason by viewModel.season
    LazyColumn(
        contentPadding = PaddingValues(top = 120.dp)
    ) {
        item {
            //赛季选择器
            SeasonHeader(initIndex = viewModel.getSeasonIndex()) { curSeason = it }
        }
        items(100){
            Row(Modifier.fillMaxWidth()){
                Text(
                    it.toString(),
                    modifier = Modifier.padding(10.dp)
                )
            }
        }
    }
}