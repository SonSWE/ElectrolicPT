package com.example.electrolicpt.DataAccess

import android.util.Log
import android.widget.Toast
import com.example.electrolicpt.ObjectInfor.ResultInfo
import com.google.firebase.database.FirebaseDatabase

class ResultDA {
    fun InsertOrUpdate(item: ResultInfo): Int {
        try {
            var _result = -1
            val db = FirebaseDatabase.getInstance().reference.child("Workout").child("Result")

            db.child(item.code!!).setValue(item).addOnSuccessListener {
                _result = 1
            }
            return _result
        } catch (e: Exception) {
            Log.e("Error: ", e.message.toString())
            return -1
        }
    }
}