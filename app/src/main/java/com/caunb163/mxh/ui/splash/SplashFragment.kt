package com.caunb163.mxh.ui.splash

import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.caunb163.domain.usecase.splash.SplashUseCase
import com.caunb163.mxh.state.State
import com.caunb163.mxh.R
import com.caunb163.mxh.base.BaseFragment
import org.koin.android.ext.android.inject

class SplashFragment : BaseFragment(R.layout.fragment_splash) {

    private val viewModel: SplashViewModel by inject()

    override fun initView(view: View) {}

    override fun initListener() {}

    override fun initObserve() {
        viewModel.initialize()
        viewModel.state.observe(this, Observer { state ->
            when (state) {
                is State.Loading -> onLoading()
                is State.Success<*> -> onSuccess(state.data as SplashUseCase.SplashState)
                is State.Failure -> onFailure()
            }
        })
    }

    private fun onLoading() {}

    private fun onSuccess(state: SplashUseCase.SplashState) {
        when (state) {
            is SplashUseCase.SplashState.OpenLogin -> findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
            is SplashUseCase.SplashState.OpenOnBoarding -> findNavController().navigate(R.id.action_splashFragment_to_onBoardingFragment)
            is SplashUseCase.SplashState.OpenHome -> findNavController().navigate(R.id.action_splashFragment_to_mainFragment)
        }
    }

    private fun onFailure() {}
}