package com.caunb163.mxh.di

import com.caunb163.mxh.ui.login.LoginViewModel
import com.caunb163.mxh.ui.main.home.HomeViewModel
import com.caunb163.mxh.ui.main.home.create_post.CreatePostViewModel
import com.caunb163.mxh.ui.main.profile.ProfileViewModel
import com.caunb163.mxh.ui.onboarding.OnBoardingViewModel
import com.caunb163.mxh.ui.register.RegisterViewModel
import com.caunb163.mxh.ui.splash.SplashViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel<SplashViewModel> { SplashViewModel(get()) }
    viewModel<OnBoardingViewModel> { OnBoardingViewModel(get()) }
    viewModel<LoginViewModel> { LoginViewModel(get()) }
    viewModel<RegisterViewModel> { RegisterViewModel(get()) }
    viewModel<HomeViewModel> { HomeViewModel(get()) }
    viewModel<CreatePostViewModel> { CreatePostViewModel(get()) }
    viewModel<ProfileViewModel> { ProfileViewModel(get()) }
}