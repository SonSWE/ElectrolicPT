package com.example.electrolicpt.ObjectInfor

import java.io.Serializable

data class ResultInfo(
    var result_Id: Int? = 0,
    var user_Id: Int? = 0,
    var lesson_Id: Int? = 0,
    var current_Date: Int? = 0,
    var current_Exercise_Id: Int? = 0,
    var count_Done: Int? = 0,
    var status: Int? = 0,
    var code: String? = ""
): Serializable
