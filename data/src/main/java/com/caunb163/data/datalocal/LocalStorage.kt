package com.caunb163.data.datalocal

import android.content.SharedPreferences

class LocalStorage(
    private val sharedPreferences: SharedPreferences
) {
    companion object {
        private const val FIRST_OPEN_APP = "first_open_app"
    }

    fun firstOpened() {
        sharedPreferences.edit().putBoolean(FIRST_OPEN_APP, false).apply()
    }

    fun isFirstOpen() = sharedPreferences.getBoolean(FIRST_OPEN_APP, true)

}