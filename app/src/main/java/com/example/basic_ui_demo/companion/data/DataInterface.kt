package com.example.basic_ui_demo.companion.data

import com.example.basic_ui_demo.data_class.person.Person
import com.example.basic_ui_demo.data_class.scorer.ScorerJson
import com.example.basic_ui_demo.data_class.teams.Team
import com.example.basic_ui_demo.data_class.teams.TeamsJson
import com.example.footballapidemo.data_class.data.Head2headJson
import com.example.footballapidemo.data_class.data.MatchesJson
import com.example.footballapidemo.data_class.data.StandingsJson
import com.example.footballapidemo.data_class.data.Teams
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface DataInterface {
    @GET("teams/")
    suspend fun getTeams(): Response<Teams>
    //OK

    @GET("teams/{teamId}/")
    suspend fun getTeamById(
        @Path("teamId") teamId: Int,
        @Query("season") season: String = "2023"
    ): Response<Team>
    //OK

    @GET("matches")
    suspend fun getMatches(
        @Query("dateFrom") dateFrom: String = "",
        @Query("dateTo") dateTo: String = "",
    ): Response<MatchesJson>
    //OK

    // 获取特定联赛的比赛数据
    @GET("competitions/{competitionCode}/matches")
    suspend fun getMatchesByCompetition(
        @Path("competitionCode") competitionCode: String,
        @Query("dateFrom") dateFrom: String = "",
        @Query("dateTo") dateTo: String = "",
    ): Response<MatchesJson>
    //OK

    //获取联赛排行榜
    @GET("competitions/{competition}/standings")
    suspend fun getCompetitionStandings(
        @Path("competition") competitionCode: String,
        @Query("season") season: String = "2023"
    ): Response<StandingsJson>

    //获取联赛射手榜
    @GET("competitions/{competition}/scorers")
    suspend fun getCompetitionScorers(
        @Path("competition") competitionCode: String,
        @Query("season") season: String = "2023",
        @Query("limit") limit: Int = 20
    ): Response<ScorerJson>


    //获取联赛teams
    @GET("competitions/{competitionCode}/teams")
    suspend fun getCompetitionTeams(
        @Path("competitionCode") competitionCode: String,
        @Query("season") season: String = "2023"
    ): Response<TeamsJson>

    //获取特定team的比赛
    @GET("teams/{teamId}/matches")
    suspend fun getMatchesByTeamId(
        @Path("teamId") teamId: Int,
        @Query("status") status: String = "",
        @Query("date") date: String = "",
        @Query("dateFrom") dateFrom: String = "",
        @Query("dateTo") dateTo: String = "",
        @Query("season") season: String = "2023"
    ): Response<MatchesJson>

    //获取某个特定person的数据
    @GET("persons/{personId}")
    suspend fun getPersonById(
        @Path("personId") personId: Int
    ): Response<Person>

    @GET("matches/{matchId}/head2head")
    suspend fun getHead2Head(
        @Path("matchId") matchId: Int
    ): Response<Head2headJson>
}