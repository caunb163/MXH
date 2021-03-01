package com.caunb163.mxh.di

import com.caunb163.data.di.localModule
import com.caunb163.data.di.repositoryModule
import com.caunb163.domain.di.useCaseModule
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
val appComponent = listOf(
    localModule,
    repositoryModule,
    viewModelModule,
    useCaseModule
)
