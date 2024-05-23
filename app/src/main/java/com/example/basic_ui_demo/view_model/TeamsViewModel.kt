package com.example.basic_ui_demo.view_model

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.basic_ui_demo.data_class.teams.Team
import com.example.basic_ui_demo.data_class.teams.TeamsJson
import com.example.basic_ui_demo.view.screen.TAG

class TeamsViewModel : ViewModel() {
    companion object {
        val competitions = listOf("英超", "法甲", "德甲", "意甲", "巴甲", "英冠")
        val competitionsCode = listOf("PL", "FL1", "BL1", "SA", "BSA", "ELC")
        //所有联赛的名字及查询代码

        val seasons = listOf("2020", "2021", "2022", "2023")
        //供下拉菜单选择的seasons列表
    }

    private val _index = mutableIntStateOf(0)
    //当前页面索引

    val index by _index
    //外露的index值

    val season = mutableStateOf("2023")
    //当前观察的season

    private val _pagesData = mutableStateListOf<PageData>()
    //页面数据列表

    private val _teamsMap = mutableMapOf<String, MutableMap<Int, Team>>()
    //存储teams详情，由season映射到teamId和team


    val isLoading = mutableStateOf(true)
    //页面是否正在加载

    val isError = mutableStateOf(false)
    //是否加载失败

    data class PageData(
        var seasonData: MutableMap<String, TeamsJson?>
    )
    //将所有页面数据封装为一个页面数据类,每个页面对应一个联赛
    //这里的seasonMap是由season指向一个TeamJson的映射集

    init {
        repeat(DataViewModel.competitions.size) { _ ->
            val map = mutableMapOf<String, TeamsJson?>()
            seasons.forEach {
                map[it] = null
            }
            _pagesData.add(PageData(map))
        }
        //初始化设置页面，将所有season对应的json设置为null
    }

    fun setIndex(index: Int) {
        Log.d(TAG, "set TeamsViewModel index: $index")
        _index.intValue = index
    }
    //set方法

    fun setPageData(teamsJson: TeamsJson, season: String) {
        if (_pagesData[_index.intValue].seasonData.containsKey(season))
            _pagesData[_index.intValue].seasonData[season] = teamsJson
        else
            throw IndexOutOfBoundsException("TeamViewModel no such season")
    }
    //set当前页面对应赛季的teamsJson

    fun getPageData(season: String): TeamsJson? {
        return if (_pagesData[_index.intValue].seasonData.containsKey(season))
            _pagesData[_index.intValue].seasonData[season]
        else
            null
    }
    //get当前页面对应赛季的teamsJson

    fun getTeam(season: String, teamId: Int): Team? {
        return _teamsMap[season]?.get(teamId)
    }

    fun addTeam(season: String, teamId: Int, team: Team) {
        _teamsMap[season]?.let {
            if (!it.containsKey(teamId))
                it[teamId] = team
        } ?: run {
            _teamsMap[season] = mutableMapOf(teamId to team)
        }
    }

    fun getSeasonIndex():Int{
        return seasons.indexOf(season.value)
    }
}