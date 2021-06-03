package com.caunb163.data.di

import com.caunb163.data.repository.*
import com.caunb163.domain.usecase.home.create_post.RepositoryCreatePost
import com.caunb163.domain.usecase.login.RepositoryLogin
import com.caunb163.domain.usecase.messenger.create_group.RepositoryCreateGroup
import com.caunb163.domain.usecase.onboarding.RepositoryOnBoarding
import com.caunb163.domain.usecase.register.RepositoryRegister
import com.caunb163.domain.usecase.splash.RepositorySplash
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.dsl.module

@ExperimentalCoroutinesApi
val repositoryModule = module {
    factory<RepositorySplash> { RepositorySplashImpl(get()) }
    factory<RepositoryOnBoarding> { RepositoryOnBoardingImpl(get()) }
    factory<RepositoryLogin> { RepositoryLoginImpl(get(), get()) }
    factory<RepositoryRegister> { RepositoryRegisterImpl(get(), get()) }
    factory<RepositoryHomeImpl> { RepositoryHomeImpl(get(), get()) }
    factory<RepositoryCreatePost> { RepositoryCreatePostImpl(get()) }
    factory<RepositoryProfileImpl> { RepositoryProfileImpl(get(), get()) }
    factory<RepositoryMainImpl> { RepositoryMainImpl(get()) }
    factory<RepositoryCommentImpl> { (RepositoryCommentImpl(get())) }
    factory<RepositoryMessengerImpl> { RepositoryMessengerImpl() }
    factory<RepositoryCreateGroup> { RepositoryCreateGroupImpl() }
    factory<RepositoryChatImpl> { RepositoryChatImpl(get()) }
    factory<RepositoryEditPostImpl> { RepositoryEditPostImpl() }
    factory<RepositoryVideoImpl> { RepositoryVideoImpl(get()) }
    factory<RepositoryUser> { RepositoryUser() }
}