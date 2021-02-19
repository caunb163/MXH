package com.caunb163.data.repository

import com.caunb163.data.datalocal.LocalStorage
import com.caunb163.domain.usecase.onboarding.RepositoryOnBoarding

class RepositoryOnBoardingImpl(
    private val localStorage: LocalStorage
) : RepositoryOnBoarding {

    override suspend fun finishOnBoarding(): Boolean {
        localStorage.firstOpened()
        return true
    }
}