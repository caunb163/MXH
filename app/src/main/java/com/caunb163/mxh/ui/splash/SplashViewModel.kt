package com.caunb163.mxh.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.caunb163.domain.state.State
import com.caunb163.domain.usecase.splash.RepositorySplash
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class SplashViewModel(
    private val repositorySplash: RepositorySplash
) : ViewModel() {

    private val _state: MutableLiveData<State> = MutableLiveData()

    val state: LiveData<State> get() = _state

    fun initialize() {
        viewModelScope.launch {
            _state.value = State.Loading

            try {
                val splashState = withContext(Dispatchers.IO) {
                    return@withContext repositorySplash.initial()
                }
                _state.value = State.Success(splashState)
            } catch (e: Exception) {
                e.printStackTrace()
                _state.value = State.Failure("${e.message}")
            }
        }
    }

}