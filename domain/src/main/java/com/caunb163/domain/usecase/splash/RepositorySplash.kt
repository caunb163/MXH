package com.caunb163.domain.usecase.splash

interface RepositorySplash {
    suspend fun initial(): SplashUseCase.SplashState
}