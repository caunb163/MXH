package com.caunb163.mxh.ui.main.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.caunb163.domain.usecase.home.HomeUseCase
import com.caunb163.mxh.state.State
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel(
    private val homeUseCase: HomeUseCase
) : ViewModel() {

    private val _state: MutableLiveData<State> = MutableLiveData()

    val state: LiveData<State> get() = _state

    fun getAllPost() {
        viewModelScope.launch {
            _state.value = State.Loading
            try {
                val homeState = withContext(Dispatchers.IO) {
                    return@withContext homeUseCase.getAllPost()
                }
                _state.value = State.Success(homeState)

            } catch (e: Exception) {
                e.printStackTrace()
                _state.value = State.Failure("${e.message}")
            }
        }
    }
}