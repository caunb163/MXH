package com.caunb163.domain.di

import com.caunb163.domain.usecase.home.create_post.CreatePostUseCase
import com.caunb163.domain.usecase.login.LoginUseCase
import com.caunb163.domain.usecase.messenger.create_group.CreateGroupUseCase
import com.caunb163.domain.usecase.onboarding.OnBoardingUseCase
import com.caunb163.domain.usecase.register.RegisterUseCase
import com.caunb163.domain.usecase.splash.SplashUseCase
import org.koin.dsl.module

val useCaseModule = module {
    factory<SplashUseCase> { SplashUseCase(get()) }
    factory<OnBoardingUseCase> { OnBoardingUseCase(get()) }
    factory<LoginUseCase> { LoginUseCase(get()) }
    factory<RegisterUseCase> { RegisterUseCase(get()) }
    factory<CreatePostUseCase> { CreatePostUseCase(get()) }
    factory<CreateGroupUseCase> { CreateGroupUseCase(get()) }
}