@file:RequiresApi(Build.VERSION_CODES.O)
@file:RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)

package com.example.basic_ui_demo.view.matches

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresExtension
import com.example.basic_ui_demo.companion.ApiViewModel
import com.example.basic_ui_demo.companion.data.RetrofitInstance
import com.example.basic_ui_demo.view_model.MatchesViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


//挂起函数，配合isLoading使用
suspend fun addMatchesByCompetitionCode(
    code: String, dateFrom: String, dateTo: String, viewModel: MatchesViewModel
) {
    val api = RetrofitInstance.api
    if (code.isNotBlank()) {
        val matchesJson = ApiViewModel.callApi {
            api.getMatchesByCompetition(code, dateFrom, dateTo)
        }
        val matches = matchesJson?.matches
        val competitionCode = MatchesViewModel.competitionsCode
        val index = competitionCode.indexOf(code)
        matches?.forEach {
            viewModel.addMatch(index, it)
        }
    } else {
        val matchesJson = ApiViewModel.callApi {
            api.getMatches(dateFrom, dateTo)
        }
        val matches = matchesJson?.matches
        matches?.forEach {
            viewModel.addMatch(0, it)
        }
    }
}

//非挂起函数，用于在LaunchEffect中做后台加载
fun initLoadingPage(
    code: String, dateFrom: String, dateTo: String, index: Int, viewModel: MatchesViewModel
) {
    //如果初始化未完成，开始执行初始化任务
    if (!viewModel.isInitDone(index))
        CoroutineScope(Dispatchers.Default).launch {
            val api = RetrofitInstance.api
            if (code.isNotBlank()) {
                val matchesJson = ApiViewModel.callApi {
                    api.getMatchesByCompetition(code, dateFrom, dateTo)
                }
                val matches = matchesJson?.matches
                matches?.forEach {
                    viewModel.addMatch(index, it)
                }
            } else {
                val matchesJson = ApiViewModel.callApi {
                    api.getMatches(dateFrom, dateTo)
                }
                val matches = matchesJson?.matches
                matches?.forEach {
                    viewModel.addMatch(0, it)
                }
            }

            viewModel.initDone(index)
        }
}