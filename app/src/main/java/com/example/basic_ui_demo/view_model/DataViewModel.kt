package com.example.basic_ui_demo.view_model

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.basic_ui_demo.data_class.scorer.ScorerJson
import com.example.basic_ui_demo.screen.TAG
import com.example.footballapidemo.data_class.data.StandingsJson

class DataViewModel : ViewModel() {
    companion object {
        val competitions = listOf("英超", "法甲", "德甲", "意甲", "巴甲", "英冠")
        val competitionsCode = listOf("PL", "FL1", "BL1", "SA", "BSA", "ELC")
        //所有联赛的名字及查询代码

        val seasons = listOf("2020", "2021", "2022", "2023")
        //供下拉菜单选择的seasons列表
    }

    private val _pagesData = mutableStateListOf<PageData>()
    //页面数据列表

    private val _index = mutableIntStateOf(0)
    //当前页面索引

    val index by _index  //供观察的外露index

    fun setIndex(index: Int) {
        Log.d(TAG, "set DataViewModel index: $index")
        _index.intValue = index
    }

    data class SeasonData(
        var standingsJson: StandingsJson?,
        var scorerJson: ScorerJson?
    )

    data class PageData(
        var seasonMap: MutableMap<String, SeasonData>
    )

    val pagesData: MutableList<PageData>
        get() = _pagesData



    init {
        repeat(competitions.size) { _ ->
            val map = mutableMapOf<String,SeasonData>()
            seasons.forEach{
                map[it] = SeasonData(null,null)
            }
            _pagesData.add(PageData(map))
        }
        //初始化设置页面,season为2023,已加载内容为null
    }


    fun setStandingsJson(standingsJson: StandingsJson, season: String) {
        _pagesData[_index.intValue].seasonMap[season]?.standingsJson = standingsJson
    }

    fun setScorerJson(scorerJson: ScorerJson, season: String) {
        _pagesData[_index.intValue].seasonMap[season]?.scorerJson = scorerJson
    }
}