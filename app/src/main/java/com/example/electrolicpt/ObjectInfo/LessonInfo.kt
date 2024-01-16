package com.example.electrolicpt.ObjectInfor

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties
import java.io.Serializable

@IgnoreExtraProperties
data class LessonInfo(
    var lesson_Id: Int? = 0,
    var name: String? = "",
    var description: String? = "",
    var level: Int? = 0,
    var lesson_Type: Int? = 0,
    var thumbnail: String? = "",
    var current_Date: Int? = 0
): Serializable{
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "lesson_Id" to lesson_Id,
            "name" to name,
            "description" to description,
            "level" to level,
            "lesson_Type" to lesson_Type,
            "thumnail" to thumbnail,
        )
    }
}
