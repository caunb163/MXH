package com.caunb163.data.di

import com.caunb163.data.repository.RepositoryLoginImpl
import com.caunb163.data.repository.RepositoryOnBoardingImpl
import com.caunb163.data.repository.RepositorySplashImpl
import com.caunb163.domain.usecase.login.RepositoryLogin
import com.caunb163.domain.usecase.onboarding.RepositoryOnBoarding
import com.caunb163.domain.usecase.splash.RepositorySplash
import org.koin.dsl.module

val repositoryModule = module {
    factory<RepositorySplash> { RepositorySplashImpl(get()) }
    factory<RepositoryOnBoarding> { RepositoryOnBoardingImpl(get()) }
    factory<RepositoryLogin> { RepositoryLoginImpl(get()) }
}