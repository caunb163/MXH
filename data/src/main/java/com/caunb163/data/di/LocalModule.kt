package com.caunb163.data.di

import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.caunb163.data.datalocal.LocalStorage
import com.caunb163.data.firebase.Auth
import com.caunb163.data.mapper.CommentMapper
import org.koin.dsl.module

val localModule = module {
    single<SharedPreferences> { PreferenceManager.getDefaultSharedPreferences(get()) }

    single<LocalStorage> { LocalStorage(get()) }

    single<Auth> { Auth() }

    single<CommentMapper> { CommentMapper() }
}