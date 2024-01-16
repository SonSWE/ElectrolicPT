package com.example.electrolicpt.ObjectInfor

import java.util.Date

data class UserInfo(
    var user_Id: Int? = 0,
    var password: String? = "",
    var phone: String? = "",
    var full_Name: String? = "",
    var birthday: Long? = 0,
    var gender: Int? = 0,
    var created_Date: Date? = null,
    var target: Int? = 0,
    var weigth: Double? = 0.0,
    var heigth: Double? = 0.0,
    var weigth_target: Double? = 0.0,
    var avt: String? = ""
)
