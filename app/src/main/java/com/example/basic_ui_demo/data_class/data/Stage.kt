package com.example.basic_ui_demo.data_class.data

enum class Stage(val value: String = "") {
    FINAL("FINAL"),
    THIRD_PLACE("THIRD_PLACE"),
    SEMI_FINALS("SEMI_FINALS"),
    QUARTER_FINALS("QUARTER_FINALS"),
    LAST_16("LAST_16"),
    LAST_32("LAST_32"),
    LAST_64("LAST_64"),
    ROUND_4("ROUND_4"),
    ROUND_3("ROUND_3"),
    ROUND_2("ROUND_2"),
    ROUND_1("ROUND_1"),
    GROUP_STAGE("GROUP_STAGE"),
    PRELIMINARY_ROUND("PRELIMINARY_ROUND"),
    QUALIFICATION("QUALIFICATION"),
    QUALIFICATION_ROUND_1("QUALIFICATION_ROUND_1"),
    QUALIFICATION_ROUND_2("QUALIFICATION_ROUND_2"),
    QUALIFICATION_ROUND_3("QUALIFICATION_ROUND_3"),
    PLAYOFF_ROUND_1("PLAYOFF_ROUND_1"),
    PLAYOFF_ROUND_2("PLAYOFF_ROUND_2"),
    PLAYOFFS("PLAYOFFS"),
    REGULAR_SEASON("REGULAR_SEASON"),
    CLAUSURA("CLAUSURA"),
    APERTURA("APERTURA"),
    CHAMPIONSHIP("CHAMPIONSHIP"),
    RELEGATION("RELEGATION"),
    RELEGATION_ROUND("RELEGATION_ROUND");

    fun getChineseDescription(): String {
        return when (this) {
            FINAL -> "总决赛"
            THIRD_PLACE -> "季军赛"
            SEMI_FINALS -> "半决赛"
            QUARTER_FINALS -> "四分之一决赛"
            LAST_16 -> "16强赛"
            LAST_32 -> "32强赛"
            LAST_64 -> "64强赛"
            ROUND_4 -> "第4轮"
            ROUND_3 -> "第3轮"
            ROUND_2 -> "第2轮"
            ROUND_1 -> "第1轮"
            GROUP_STAGE -> "小组赛"
            PRELIMINARY_ROUND -> "预赛"
            QUALIFICATION -> "资格赛"
            QUALIFICATION_ROUND_1 -> "资格赛第1轮"
            QUALIFICATION_ROUND_2 -> "资格赛第2轮"
            QUALIFICATION_ROUND_3 -> "资格赛第3轮"
            PLAYOFF_ROUND_1 -> "季后赛第1轮"
            PLAYOFF_ROUND_2 -> "季后赛第2轮"
            PLAYOFFS -> "季后赛"
            REGULAR_SEASON -> "常规赛"
            CLAUSURA -> "闭幕赛"
            APERTURA -> "开幕赛"
            CHAMPIONSHIP -> "冠军赛"
            RELEGATION -> "降级赛"
            RELEGATION_ROUND -> "降级赛轮次"
        }
    }
}