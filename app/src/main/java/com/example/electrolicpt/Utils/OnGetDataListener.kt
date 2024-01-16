package com.example.electrolicpt.Utils

import com.google.firebase.database.DataSnapshot

interface OnGetDataListener {
    fun onSuccess(snapshot: DataSnapshot)
}