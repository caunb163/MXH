package com.caunb163.mxh

import android.app.Application
import com.caunb163.mxh.di.appComponent
import com.google.firebase.FirebaseApp
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MyApplication)
            modules(appComponent)
        }
    }

}