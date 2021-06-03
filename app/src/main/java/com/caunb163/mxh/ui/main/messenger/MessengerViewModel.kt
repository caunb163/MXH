package com.caunb163.mxh.ui.main.messenger

import androidx.lifecycle.*
import com.caunb163.data.datalocal.LocalStorage
import com.caunb163.data.repository.RepositoryMessengerImpl
import com.caunb163.mxh.state.State
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MessengerViewModel(
    private val repositoryMessengerImpl: RepositoryMessengerImpl,
    private val localStorage: LocalStorage
) : ViewModel() {
    val listener: LiveData<State> = liveData(Dispatchers.IO) {
        emit(State.Loading)
        try {
            repositoryMessengerImpl.listenerGroupChange(localStorage.getAccount()!!.userId)
                .collect {
                    emit(State.Success(it))
                }
        } catch (e: Exception) {
            emit(State.Failure("${e.message}"))
        }
    }
}