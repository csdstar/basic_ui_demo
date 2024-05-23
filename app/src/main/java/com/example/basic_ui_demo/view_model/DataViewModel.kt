package com.example.basic_ui_demo.view_model

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.basic_ui_demo.data_class.scorer.ScorerJson
import com.example.basic_ui_demo.view.screen.TAG
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

    val pagesData: MutableList<PageData>
        get() = _pagesData
    //外露的页面数据状态

    private val _index = mutableIntStateOf(0)
    //当前页面索引

    val index by _index
    //外露的index

    fun setIndex(index: Int) {
        Log.d(TAG, "set DataViewModel index: $index")
        _index.intValue = index
    }

    fun setStandingsJson(standingsJson: StandingsJson, season: String) {
        _pagesData[_index.intValue].seasonMap[season]?.standingsJson = standingsJson
    }
    //修改当前index下的页面数据的standingJson

    fun setScorerJson(scorerJson: ScorerJson, season: String) {
        _pagesData[_index.intValue].seasonMap[season]?.scorerJson = scorerJson
    }
    //修改当前index下的页面数据的scorerJson

    data class PageData(
        var seasonMap: MutableMap<String, SeasonData>
    )
    //将所有页面数据封装为一个页面数据类,每个页面对应一个联赛
    //这里的seasonMap是由season指向两个json的映射集

    data class SeasonData(
        var standingsJson: StandingsJson?,
        var scorerJson: ScorerJson?
    )

    init {
        repeat(competitions.size) { _ ->
            val map = mutableMapOf<String, SeasonData>()
            seasons.forEach {
                map[it] = SeasonData(null, null)
            }
            _pagesData.add(PageData(map))
        }
        //初始化设置页面，将所有season对应的json设置为null
    }
}