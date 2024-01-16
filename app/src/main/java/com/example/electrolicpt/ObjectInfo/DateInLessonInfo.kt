package com.example.electrolicpt.ObjectInfor

data class DateInLessonInfo(
    var date_Id: Int? = 0,
    var lesson_Id: Int? = 0,
    var day: Int? =0,
    var name: String? = "",
    var description: String? = "",
    var time: Int? = 0,
    var status: Int? = 0,
    var thumbnail: String? = "",
    var countDone: Int? =0,
    var user_Id: Int? = 0,
)