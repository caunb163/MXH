package com.caunb163.domain.usecase.onboarding

class OnBoardingUseCase(
    private val repositoryOnBoarding: RepositoryOnBoarding
) {
    suspend operator fun invoke(): Boolean {
        return repositoryOnBoarding.finishOnBoarding()
    }
}