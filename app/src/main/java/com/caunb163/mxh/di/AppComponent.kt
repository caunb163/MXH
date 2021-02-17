package com.caunb163.mxh.di

import com.caunb163.data.di.localModule
import com.caunb163.data.di.repositoryModule

val appComponent = listOf(
    localModule,
    repositoryModule,
    viewModelModule
)
