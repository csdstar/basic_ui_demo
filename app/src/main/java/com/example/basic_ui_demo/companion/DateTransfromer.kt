@file:RequiresApi(Build.VERSION_CODES.O)

package com.example.basic_ui_demo.companion

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

//将UTC时间转换为LocalDate格式的中国日期
fun convertUtcToChinaLocalDate(utcTimeString: String?): LocalDate {
    // 解析UTC时间字符串为Instant对象
    val utcTime = Instant.parse(utcTimeString)
    // 获取中国时区
    val chinaZoneId = ZoneId.of("Asia/Shanghai")
    // 将UTC时间转换为中国时区时间
    val chinaTime = ZonedDateTime.ofInstant(utcTime, chinaZoneId)
    return chinaTime.toLocalDate()
}

//将字符串格式的UTC时间转换为字符串格式中国时间
fun convertUtcToChinaTime(utcTimeString: String?): String {
    val utcDateTime = LocalDateTime.parse(utcTimeString, DateTimeFormatter.ISO_DATE_TIME)
    val utcZoneId = ZoneId.of("UTC")
    val chinaZoneId = ZoneId.of("Asia/Shanghai")
    val utcZonedDateTime = ZonedDateTime.of(utcDateTime, utcZoneId)
    val chinaZonedDateTime = utcZonedDateTime.withZoneSameInstant(chinaZoneId)
    val formatter = DateTimeFormatter.ofPattern("HH:mm")
    return formatter.format(chinaZonedDateTime)
}

//将字符串格式的UTC时间转换为字符串格式中国日期
fun convertUtcToChinaCertainDate(utcTimeString: String?): String {
    val chinaLocalDate = convertUtcToChinaLocalDate(utcTimeString)
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    return chinaLocalDate.format(formatter)
}

fun convertUtcToChinaDate(utcTimeString: String?):String{
    val chinaLocalDate = convertUtcToChinaLocalDate(utcTimeString)
    val formatter = DateTimeFormatter.ofPattern("MM-dd")
    return chinaLocalDate.format(formatter)
}

fun convertDateStringToYearAndMonth(date: String): String {
    //提取"YYYY-MM-DD"格式的日期中的年份和月份，方便排序(无错误检查)
    val parts = date.split("-")
    return parts[0] + "-" + parts[1]
}

//获取字符串格式的当前日期
fun getCurrentDateString(): String {
    val currentDate = LocalDate.now()
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    return currentDate.format(formatter)
}

//由字符串日期和days来获取推移若干天的字符串格式日期
fun getDateStringWithOffset(date: String, days: Long, isForward: Boolean): String {
    // 将输入的日期字符串解析为 LocalDate 对象
    val currentDate = LocalDate.parse(date)

    // 根据isForward来决定添加或减去指定的天数
    val resultDate = if (isForward) {
        currentDate.plusDays(days)
    } else {
        currentDate.minusDays(days)
    }

    // 格式化日期并返回
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    return resultDate.format(formatter)
}

//从年月日三个变量中返回字符串格式的日期
fun formatDate(year: String, month: String, day: String): String {
    val yearInt = year.toInt()
    val monthInt = month.toInt()
    val dayInt = day.toInt()

    val date = LocalDate.of(yearInt, monthInt, dayInt)
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    return date.format(formatter)
}

//检测两个日期前后顺序是否合法
fun isDateValid(dateFrom: String, dateTo: String): Boolean {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val fromDate = LocalDate.parse(dateFrom, formatter)
    val toDate = LocalDate.parse(dateTo, formatter)
    return !fromDate.isAfter(toDate)
}

fun isDateInvalid(dateFrom: String, dateTo: String): Boolean {
    return !isDateValid(dateFrom,dateTo)
}

