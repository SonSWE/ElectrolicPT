package com.example.electrolicpt.DataAccess



import com.example.electrolicpt.ObjectInfor.UserInfo
import com.example.electrolicpt.ObjectInfor.UserLessonInfo

import com.google.firebase.database.FirebaseDatabase



class UserLessonDA {
    private val reference = FirebaseDatabase.getInstance().getReference("Workout/User_Lesson")

    private var instance: UserLessonDA? = null

    @Synchronized
    fun getInstance(): UserLessonDA? {
        if (instance == null) {
            instance = UserLessonDA()
        }
        return instance
    }

    fun Insert(user: UserInfo): Int {
        var userLesson = UserLessonInfo(0,user.user_Id,1)

        if(user?.target == 0){
            userLesson.lesson_Id = 1
        }else if(user?.target == 1){
            userLesson.lesson_Id = 1
        }else if(user?.target == 2){
            userLesson.lesson_Id = 1
        }
        reference.push().setValue(userLesson)

        return 1
    }


}