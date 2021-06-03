package com.caunb163.mxh.ui.main.messenger

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.caunb163.data.datalocal.LocalStorage
import com.caunb163.data.repository.RepositoryGroupImpl
import com.caunb163.mxh.state.State
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect

class GroupViewModel(
    private val repositoryGroupImpl: RepositoryGroupImpl,
    private val localStorage: LocalStorage
) : ViewModel() {
    val listener: LiveData<State> = liveData(Dispatchers.IO) {
        emit(State.Loading)
        try {
            repositoryGroupImpl.listenerGroupChange(localStorage.getAccount()!!.userId)
                .collect {
                    emit(State.Success(it))
                }
        } catch (e: Exception) {
            emit(State.Failure("${e.message}"))
        }
    }
}