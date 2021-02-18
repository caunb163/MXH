package com.caunb163.mxh.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.caunb163.domain.usecase.register.RegisterUseCase
import com.caunb163.mxh.state.State
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegisterViewModel(
    private val registerUseCase: RegisterUseCase
) : ViewModel() {
    private val _state: MutableLiveData<State> = MutableLiveData()

    val state: LiveData<State> get() = _state

    fun createAccount(username: String, email: String, password: String, phone: String) {
        viewModelScope.launch {
            _state.value = State.Loading
            try {
                val registerState = withContext(Dispatchers.IO) {
                    return@withContext registerUseCase.register(username, email, password, phone)
                }

                _state.value = State.Success(registerState)

            } catch (e: Exception) {
                e.printStackTrace()
                _state.value = State.Failure("${e.message}")
            }
        }
    }

}