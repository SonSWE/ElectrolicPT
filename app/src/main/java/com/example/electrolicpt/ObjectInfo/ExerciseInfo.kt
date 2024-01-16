package com.example.electrolicpt.ObjectInfor

import java.io.Serializable

data class ExerciseInfo (
    val exercise_Id: Int? = 0,
    val exercise_Name: String? = "",
    val description: String? = "",
    val time: Int? = 0,
    val type: Int? = 0,
    val thumbnail: String? = "",
    val video_Id: String? = "",
    val tutorial: String? = ""
): Serializable
