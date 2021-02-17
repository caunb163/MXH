package com.caunb163.mxh.di

import com.caunb163.mxh.ui.onboarding.OnBoardingViewModel
import com.caunb163.mxh.ui.splash.SplashViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel<SplashViewModel> { SplashViewModel(get()) }
    viewModel<OnBoardingViewModel> { OnBoardingViewModel(get()) }
}