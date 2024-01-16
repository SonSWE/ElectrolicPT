package com.example.electrolicpt.Utils

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.annotation.NonNull
import androidx.compose.ui.graphics.Color
import com.example.electrolicpt.R


class LoadingDialog(context: Context) : Dialog(context) {
    init {
        val params = window?.attributes
        params?.gravity = Gravity.CENTER
        window?.attributes = params
        window?.setBackgroundDrawableResource(android.R.color.transparent)
        setTitle(null)
        setCancelable(false)
        setCanceledOnTouchOutside(false)  // Tương đương với setOnCancelListener(null) trong Java
        val view = LayoutInflater.from(context).inflate(R.layout.loading_dialog, null)
        setContentView(view)
    }
}