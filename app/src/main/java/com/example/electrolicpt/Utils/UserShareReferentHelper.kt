package com.example.electrolicpt.Utils

import android.content.Context
import com.example.electrolicpt.ObjectInfor.UserInfo

import com.google.gson.Gson

class UserShareReferentHelper {

    fun saveUser(user: UserInfo, context: Context) {
        val preferences =  context.getSharedPreferences("USER_LOGIN", Context.MODE_PRIVATE)
        val gson = Gson()
        val user = gson.toJson(user)
        preferences.edit().putString("USER_VALUE",user).apply()
    }

    fun getUser(context: Context): UserInfo {
        val preferences = context.getSharedPreferences("USER_LOGIN", Context.MODE_PRIVATE)
        val gson = Gson()
        val user = preferences.getString("USER_VALUE", null)
        return gson.fromJson(user, UserInfo::class.java)
    }

    fun removeUser(context: Context) {
        val sharedPref = context.getSharedPreferences("USER_LOGIN", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.remove("USER_VALUE")
        editor.apply()
    }

}