package com.example.electrolicpt.ObjectInfor

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties
import java.io.Serializable

@IgnoreExtraProperties
data class ExerciseLessonInfo(
    var exercise_Id: Int? = 0,
    var lesson_Id: Int? = 1,
    var exercise_Lesson_Id: Int? = 0,
    var date_Id: Int? = 0
): Serializable{
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "exercise_Id" to exercise_Id,
            "lesson_Id" to lesson_Id,
            "exercise_Lesson_Id" to exercise_Lesson_Id,
            "date_Id" to date_Id
        )
    }
}
