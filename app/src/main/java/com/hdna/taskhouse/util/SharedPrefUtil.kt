package com.hdna.taskhouse.util

import android.content.Context
import android.content.SharedPreferences

class SharedPrefUtil (context: Context) {

    private val PREFS_NAME = "mainPrefStorage"
    private var sharedPref: SharedPreferences
    val editor: SharedPreferences.Editor

    init {
        sharedPref = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        editor = sharedPref.edit()
    }

    fun put(key: String, value: String) {
        editor.putString(key, value)
            .apply()
    }

    fun put(key: String, value: Boolean) {
        editor.putBoolean(key, value)
            .apply()
    }

    fun put(key: String, value: Int) {
        editor.putInt(key, value)
            .apply()
    }

    fun put(key: String, value: Float) {
        editor.putFloat(key, value)
            .apply()
    }

    fun put(key: String, value: Long) {
        editor.putLong(key, value)
            .apply()
    }

    fun getBoolean(key: String): Boolean {
        return sharedPref.getBoolean(key, false)
    }

    fun getString(key: String): String? {
        return sharedPref.getString(key, null)
    }

    fun getInt(key: String): Int {
        return sharedPref.getInt(key, 0)
    }

    fun getFloat(key: String): Float {
        return sharedPref.getFloat(key, 0F)
    }

    fun getLong(key: String): Long {
        return sharedPref.getLong(key, 0L)
    }



    fun clear() {
        editor.clear()
            .apply()
    }

}