package com.example.footballapidemo.data_class.data

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.basic_ui_demo.data_class.data.Status
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

data class Match(
    val area: Area?,
    val attendance: Any?,
    val awayTeam: Team,
    val bookings: List<Any>?,
    val competition: Competition,
    val goals: List<Any>?,
    val group: String?,
    val homeTeam: Team,
    val id: Int,
    val injuryTime: Any?,
    val lastUpdated: String?,
    val matchday: Int?,
    val minute: Any?,
    val odds: Odds?,
    val penalties: List<Any>?,
    val referees: List<Any>?,
    val score: Score,
    val season: Season?,
    val stage: Stage,
    val status: Status?,
    val substitutions: List<Any>?,
    val utcDate: String?,
    val venue: String?
) {
    //将UTC时间转换为LocalDate格式的中国日期
    @RequiresApi(Build.VERSION_CODES.O)
    fun convertUtcToChinaLocalDate(): LocalDate {
        // 解析UTC时间字符串为Instant对象
        val utcTime = Instant.parse(utcDate)
        // 获取中国时区
        val chinaZoneId = ZoneId.of("Asia/Shanghai")
        // 将UTC时间转换为中国时区时间
        val chinaTime = ZonedDateTime.ofInstant(utcTime, chinaZoneId)
        return chinaTime.toLocalDate()
    }

    //将字符串格式的UTC时间转换为字符串格式中国时间
    @RequiresApi(Build.VERSION_CODES.O)
    fun convertUtcToChinaTime(): String {
        val utcDateTime = LocalDateTime.parse(utcDate, DateTimeFormatter.ISO_DATE_TIME)
        val utcZoneId = ZoneId.of("UTC")
        val chinaZoneId = ZoneId.of("Asia/Shanghai")
        val utcZonedDateTime = ZonedDateTime.of(utcDateTime, utcZoneId)
        val chinaZonedDateTime = utcZonedDateTime.withZoneSameInstant(chinaZoneId)
        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        return formatter.format(chinaZonedDateTime)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun convertUtcToChinaDate(): String {
        val chinaLocalDate = convertUtcToChinaLocalDate()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        return chinaLocalDate.format(formatter)
    }
}