package com.caunb163.mxh.ui.login.phone

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.caunb163.domain.usecase.login.LoginUseCase
import com.caunb163.mxh.state.State
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PhoneViewModel(
    private val loginUserCase: LoginUseCase
) : ViewModel() {
    private val _stateUser: MutableLiveData<State> = MutableLiveData()
    val stateUser: LiveData<State> get() = _stateUser
    fun getUserPhone() {
        viewModelScope.launch {
            _stateUser.value = State.Loading
            try {
                val phoneState = withContext(Dispatchers.IO) {
                    return@withContext loginUserCase.getUserPhone()
                }
                _stateUser.value = State.Success(phoneState)
            } catch (e: Exception) {
                _stateUser.value = State.Failure("${e.message}")
            }
        }
    }

    private val _stateCheck: MutableLiveData<State> = MutableLiveData()
    val stateCheck: LiveData<State> get() = _stateCheck
    fun checkPhone(verificationId: String, opt: String) {
        viewModelScope.launch {
            _stateCheck.value = State.Loading
            try {
                val checkState = withContext(Dispatchers.IO) {
                    return@withContext loginUserCase.checkPhone(verificationId, opt)
                }
                _stateCheck.value = State.Success(checkState)
            } catch (e: Exception) {
                _stateCheck.value = State.Failure("${e.message}")
            }
        }
    }

    private val _stateCreateUser: MutableLiveData<State> = MutableLiveData()
    val stateCreateUser: LiveData<State> get() = _stateCreateUser
    fun createUser(username: String, birthday: String, gender: String) {
        viewModelScope.launch {
            _stateCreateUser.value = State.Loading
            try {
                val createState = withContext(Dispatchers.IO) {
                    return@withContext loginUserCase.createUser(username, birthday, gender)
                }
                _stateCreateUser.value = State.Success(createState)
            } catch (e: Exception) {
                _stateCreateUser.value = State.Failure("${e.message}")
            }
        }
    }
}