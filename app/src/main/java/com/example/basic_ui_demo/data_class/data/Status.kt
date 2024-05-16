package com.example.basic_ui_demo.data_class.data

enum class Status {
    SCHEDULED {
        override fun toChineseString() = "计划中"
    },
    LIVE {
        override fun toChineseString() = "直播中"
    },
    IN_PLAY {
        override fun toChineseString() = "进行中"
    },
    PAUSED {
        override fun toChineseString() = "暂停"
    },
    FINISHED {
        override fun toChineseString() = "已结束"
    },
    POSTPONED {
        override fun toChineseString() = "推迟"
    },
    SUSPENDED {
        override fun toChineseString() = "暂停"
    },
    CANCELLED {
        override fun toChineseString() = "取消"
    };

    abstract fun toChineseString(): String
}
