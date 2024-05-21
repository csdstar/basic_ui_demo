package com.example.basic_ui_demo.view_model

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.lifecycle.ViewModel
import com.example.basic_ui_demo.screen.TAG

class TeamsViewModel : ViewModel() {
    companion object {
        val competitions = listOf("英超", "法甲", "德甲", "意甲", "巴甲", "英冠")
        val competitionsCode = listOf("PL", "FL1", "BL1", "SA", "BSA", "ELC")
        //所有联赛的名字及查询代码
    }

    private val _index = mutableIntStateOf(0)
    //当前页面索引

    val index by _index

    fun setIndex(index: Int) {
        Log.d(TAG,"set TeamsViewModel index: $index")
        _index.intValue = index
    }
}