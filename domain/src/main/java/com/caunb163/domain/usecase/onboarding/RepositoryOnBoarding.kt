package com.caunb163.domain.usecase.onboarding

interface RepositoryOnBoarding {
    suspend fun finishOnBoarding(): Boolean
}