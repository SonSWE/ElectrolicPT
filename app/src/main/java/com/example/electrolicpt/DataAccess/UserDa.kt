package com.example.electrolicpt.DataAccess


import android.content.Context
import android.util.Log
import com.example.electrolicpt.ObjectInfor.UserInfo
import com.example.electrolicpt.Utils.CommomFunc
import com.example.electrolicpt.Utils.LoadingDialog
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.getValue
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await


class UserDa {
    private val reference = FirebaseDatabase.getInstance().getReference("Auth/Ept_User")

    private var instance: UserDa? = null

    @Synchronized
    fun getInstance(): UserDa? {
        if (instance == null) {
            instance = UserDa()
        }
        return instance
    }

    fun Insert(user: UserInfo): Task<Int> {
        //lấy id lớn nhất hiện tại trong danh sách user để tạo id cho user mới
        return GetMaxId().addOnSuccessListener {
            user.user_Id = it + 1

            reference.push().setValue(user)
                .continueWithTask<Int>(Continuation<Void?, Task<Int>> { task: Task<Void?> ->
                    if (task.isSuccessful) {
                        UserLessonDA().Insert(user)
                        return@Continuation Tasks.forResult(user.user_Id)
                    } else {
                        return@Continuation Tasks.forResult(-1)
                    }
                })

        }.addOnFailureListener { e ->
            Log.e("LOG ERROR | ", e.message.toString())
        }
    }

    fun getUserList(): Task<ArrayList<UserInfo>>? {
        val list: ArrayList<UserInfo> = ArrayList<UserInfo>()
        return reference.get().continueWithTask<ArrayList<UserInfo>> { task: Task<DataSnapshot> ->
            if (task.isSuccessful) {
                val snapshot = task.result
                if (snapshot.exists()) {
                    for (dataSnapshot in snapshot.children) {
                        val model = dataSnapshot.getValue<UserInfo>()
                        if (model != null) {
                            list.add(model)
                        }
                    }
                }
                return@continueWithTask Tasks.forResult<ArrayList<UserInfo>>(list)
            } else {
                return@continueWithTask Tasks.forResult<ArrayList<UserInfo>>(null)
            }
        }
    }

    fun LoginV3(tvPhone: String, password: String): Task<UserInfo>? {
        var _user: UserInfo? = null
        return reference.get().continueWithTask<UserInfo> { task: Task<DataSnapshot> ->
            if (task.isSuccessful) {
                val snapshot = task.result
                if (snapshot.exists()) {
                    for (dataSnapshot in snapshot.children) {
                        val model = dataSnapshot.getValue<UserInfo>()
                        val passEnscryp = CommomFunc().encrypt(password).toString().trim()
                        val phoneNumber = "+" + tvPhone.trim()

                        if (model != null && model.phone == phoneNumber && model.password == passEnscryp) {
                            _user = model
                            break
                        }
                    }
                }
                return@continueWithTask Tasks.forResult<UserInfo>(_user)
            } else {
                return@continueWithTask Tasks.forResult<UserInfo>(null)
            }
        }
    }

    fun GetByPhone(phoneNumber: String): Task<UserInfo>? {
        var _user: UserInfo? = null
        return reference.get().continueWithTask<UserInfo> { task: Task<DataSnapshot> ->
            if (task.isSuccessful) {
                val snapshot = task.result
                if (snapshot.exists()) {
                    for (dataSnapshot in snapshot.children) {
                        val model = dataSnapshot.getValue<UserInfo>()
                        if (model != null && model.phone == phoneNumber) {
                            _user = model
                        }
                    }
                }
                return@continueWithTask Tasks.forResult<UserInfo>(_user)
            } else {
                return@continueWithTask Tasks.forResult<UserInfo>(null)
            }
        }
    }

    fun GetMaxId(): Task<Int> {
        var max_id = 0
        return reference.get().continueWithTask<Int> { task: Task<DataSnapshot> ->
            if (task.isSuccessful) {
                val snapshot = task.result
                if (snapshot.exists()) {
                    for (dataSnapshot in snapshot.children) {
                        val model = dataSnapshot.getValue<UserInfo>()
                        if (model != null && model.user_Id!! > max_id) {
                            max_id = model.user_Id!!
                        }
                    }
                }
                return@continueWithTask Tasks.forResult<Int>(max_id)
            } else {
                return@continueWithTask Tasks.forResult<Int>(0)
            }
        }
    }
}