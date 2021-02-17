package com.caunb163.domain.usecase.splash

class SplashUseCase(
    private val repositorySplash: RepositorySplash
) {
    suspend operator fun invoke(): Boolean {
        return repositorySplash.initial()
    }
}