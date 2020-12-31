package com.linh.doan.helper

import android.content.Context
import android.content.SharedPreferences

object PrefsHelper {
    private lateinit var prefs: SharedPreferences

    private const val PREFS_NAME = "CHATAPP"

    const val LOGIN = "LOGIN"
    const val LANGUAGE = "LANGUAGE"

    fun init(context: Context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun readString(key: String, defaultValue: String): String? {
        return prefs.getString(key, defaultValue)
    }

    fun readLong(key: String, defaultValue: Long): Long? {
        return prefs.getLong(key, defaultValue)
    }

    fun  readBoolean (key : String, defaultValue: Boolean) : Boolean?{
        return  prefs.getBoolean(key, defaultValue)
    }



    fun writeString(key: String, defaultValue: String) {
        val prefsEditor: SharedPreferences.Editor = prefs.edit()
        with(prefsEditor) {
            putString(key, defaultValue)
            commit()
        }
    }

    fun writeLong(key: String, defaultValue: Long) {
        val prefsEditor: SharedPreferences.Editor = prefs.edit()
        with(prefsEditor) {
            putLong(key, defaultValue)
            commit()
        }
    }

    fun writeBoolean(key: String, defaultValue: Boolean) {
        val prefsEditor: SharedPreferences.Editor = prefs.edit()
        with(prefsEditor) {
            putBoolean(key, defaultValue)
            commit()
        }
    }
}