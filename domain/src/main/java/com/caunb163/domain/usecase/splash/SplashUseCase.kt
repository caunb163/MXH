package com.caunb163.domain.usecase.splash

class SplashUseCase(
    private val repositorySplash: RepositorySplash
) {
    suspend operator fun invoke(): SplashState {
        return repositorySplash.initial()
    }

    sealed class SplashState {
        object OpenOnBoarding : SplashState()

        object OpenLogin : SplashState()

        object OpenHome : SplashState()
    }
}