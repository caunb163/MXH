package com.caunb163.data.repository

import com.caunb163.data.datalocal.LocalStorage
import com.caunb163.domain.usecase.splash.RepositorySplash
import com.caunb163.domain.usecase.splash.SplashUseCase

class RepositorySplashImpl(
    private val localStorage: LocalStorage
) : RepositorySplash {
    override suspend fun initial(): SplashUseCase.SplashState {
        if (localStorage.isFirstOpen()) {
            return SplashUseCase.SplashState.OpenOnBoarding
        } else if (localStorage.getAccount() != null) {
            return SplashUseCase.SplashState.OpenHome
        } else return SplashUseCase.SplashState.OpenLogin
    }
}