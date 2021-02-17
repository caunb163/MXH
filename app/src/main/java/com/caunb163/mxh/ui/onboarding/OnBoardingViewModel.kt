package com.caunb163.mxh.ui.onboarding

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.caunb163.domain.state.State
import com.caunb163.domain.usecase.onboarding.RepositoryOnBoarding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class OnBoardingViewModel(
    private val repositoryOnBoarding: RepositoryOnBoarding
) : ViewModel() {

    private val _state: MutableLiveData<State> = MutableLiveData()

    val state: LiveData<State> get() = _state

    fun finishOnBoarding() {
        viewModelScope.launch {
            _state.value = State.Loading

            try {
                val onBoardingState = withContext(Dispatchers.IO) {
                    return@withContext repositoryOnBoarding.finishOnBoarding()
                }
                _state.value = State.Success(onBoardingState)
            } catch (e: Exception) {
                e.printStackTrace()
                _state.value = State.Failure("${e.message}")
            }
        }
    }
}