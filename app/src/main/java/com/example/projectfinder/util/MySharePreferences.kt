package com.example.projectfinder.util

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity

class MySharePreferences(context: Context)
{
    companion object
    {
        const val FILE_NAME: String = "SHARED_PREF_FILE"
        const val ACCESS_TOKEN: String = "ACCESS_TOKEN"
        const val REFRESH_TOKEN: String = "REFRESH_TOKEN"
        const val LOGIN: String = "LOGIN"
        const val PASSWORD: String = "PASSWORD"
    }

    private val mySharedPreferences: SharedPreferences = context.getSharedPreferences(FILE_NAME, AppCompatActivity.MODE_PRIVATE)
    private val myEditor: SharedPreferences.Editor = mySharedPreferences.edit()

    fun setAccessToken(accessToken: String){
        myEditor.putString(ACCESS_TOKEN, accessToken)
        myEditor.apply()
    }

    fun getAccessToken(): String? {
        return mySharedPreferences.getString(ACCESS_TOKEN, "")
    }

    fun setRefreshToken(refreshToken: String){
        myEditor.putString(REFRESH_TOKEN, refreshToken)
        myEditor.apply()
    }

    fun getRefreshToken(): String? {
        return mySharedPreferences.getString(REFRESH_TOKEN, "")
    }

    fun setLogin(login: String){
        myEditor.putString(LOGIN, login)
        myEditor.apply()
    }

    fun getLogin(): String? {
        return mySharedPreferences.getString(LOGIN, "")
    }

    fun setPassword(password: String){
        myEditor.putString(PASSWORD, password)
        myEditor.apply()
    }

    fun getPassword(): String? {
        return mySharedPreferences.getString(PASSWORD, "")
    }
}