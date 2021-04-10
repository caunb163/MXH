package com.caunb163.mxh.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.caunb163.domain.usecase.login.LoginUseCase
import com.caunb163.mxh.state.State
import com.google.android.gms.auth.api.credentials.IdToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _state: MutableLiveData<State> = MutableLiveData()
    val state: LiveData<State> get() = _state
    fun loginWithEmailAndPassword(email: String, password: String) {
        viewModelScope.launch {
            _state.value = State.Loading
            try {
                val loginState = withContext(Dispatchers.IO) {
                    return@withContext loginUseCase.loginWithEmailAndPassword(email, password)
                }
                _state.value = State.Success(loginState)

            } catch (e: Exception) {
                _state.value = State.Failure("${e.message}")
            }
        }
    }

    private val _stateGoogle: MutableLiveData<State> = MutableLiveData()
    val stateGoogle: LiveData<State> get() = _stateGoogle
    fun loginWithGoogle(idToken: String) {
        viewModelScope.launch {
            _stateGoogle.value = State.Loading
            try {
                val googleState = withContext(Dispatchers.IO) {
                    return@withContext loginUseCase.loginWithGoogle(idToken)
                }
                _stateGoogle.value = State.Success(googleState)
            } catch (e: Exception) {
                _stateGoogle.value = State.Failure("${e.message}")
            }
        }
    }

}