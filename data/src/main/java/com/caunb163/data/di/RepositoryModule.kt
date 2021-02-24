package com.caunb163.data.di

import com.caunb163.data.repository.*
import com.caunb163.domain.usecase.home.create_post.RepositoryCreatePost
import com.caunb163.domain.usecase.login.RepositoryLogin
import com.caunb163.domain.usecase.onboarding.RepositoryOnBoarding
import com.caunb163.domain.usecase.register.RepositoryRegister
import com.caunb163.domain.usecase.splash.RepositorySplash
import org.koin.dsl.module

val repositoryModule = module {
    factory<RepositorySplash> { RepositorySplashImpl(get()) }
    factory<RepositoryOnBoarding> { RepositoryOnBoardingImpl(get()) }
    factory<RepositoryLogin> { RepositoryLoginImpl(get(), get()) }
    factory<RepositoryRegister> { RepositoryRegisterImpl(get()) }
    factory<RepositoryHomeImpl> { RepositoryHomeImpl() }
    factory<RepositoryCreatePost> { RepositoryCreatePostImpl() }
}