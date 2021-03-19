package com.caunb163.mxh.ui.main.messenger

import android.util.Log
import androidx.lifecycle.*
import com.caunb163.data.datalocal.LocalStorage
import com.caunb163.data.repository.RepositoryGroupImpl
import com.caunb163.mxh.state.State
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MessengerViewModel(
    private val repositoryGroupImpl: RepositoryGroupImpl,
    private val localStorage: LocalStorage
) : ViewModel() {
    private val _state: MutableLiveData<State> = MutableLiveData()
    val state: LiveData<State> get() = _state

    fun getAllMyGroup() {
        viewModelScope.launch {
            _state.value = State.Loading
            try {
                val stateGroup = withContext(Dispatchers.IO) {
                    return@withContext repositoryGroupImpl.getAllMyGroup(localStorage.getAccount()!!.userId)
                }
                _state.value = State.Success(stateGroup)
            } catch (e: Exception) {
                _state.value = State.Failure("${e.message}")
            }
        }
    }

//    val listener: LiveData<State> = liveData(Dispatchers.IO) {
//        emit(State.Loading)
//        try {
//            repositoryGroupImpl.listenerGroupChange(localStorage.getAccount()!!.userId).collect {
//                emit(State.Success(it))
//            }
//        } catch (e: Exception) {
//            emit(State.Failure("${e.message}"))
//        }
//    }
}