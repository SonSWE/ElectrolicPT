package com.example.electrolicpt.ObjectInfor

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class AlarmDataInfo(
    val hour: Int = 0,
    val minute: Int = 0,
    val selectedDays: List<String> = emptyList(),
    val notificationText: String = "",
    val am: Boolean = true,

)