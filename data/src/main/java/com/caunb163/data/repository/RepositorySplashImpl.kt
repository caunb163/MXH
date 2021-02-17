package com.caunb163.data.repository

import com.caunb163.data.datalocal.LocalStorage
import com.caunb163.domain.usecase.splash.RepositorySplash

class RepositorySplashImpl(
    private val localStorage: LocalStorage
) : RepositorySplash {
    override suspend fun initial(): Boolean {
        return localStorage.isFirstOpen()
    }
}