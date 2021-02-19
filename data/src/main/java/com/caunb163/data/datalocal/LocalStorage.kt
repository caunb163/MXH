package com.caunb163.data.datalocal

import android.content.SharedPreferences
import com.caunb163.domain.model.User
import com.google.gson.Gson

class LocalStorage(
    private val sharedPreferences: SharedPreferences
) {

    private val gson = Gson()

    companion object {
        private const val FIRST_OPEN_APP = "first_open_app"
        private const val SAVE_ACCOUNT = "save_account"
    }

    fun firstOpened() {
        sharedPreferences.edit().putBoolean(FIRST_OPEN_APP, false).apply()
    }

    fun isFirstOpen() = sharedPreferences.getBoolean(FIRST_OPEN_APP, true)

    fun getAccount(): User? {
        val user = sharedPreferences.getString(SAVE_ACCOUNT, "")
        return if (user?.isNotEmpty() == true)
            gson.fromJson(user, User::class.java)
        else null
    }

    fun saveAccount(user: User?) {
        sharedPreferences.edit().putString(SAVE_ACCOUNT, gson.toJson(user)).apply()
    }


}