package com.caunb163.mxh.ui.main.video

import androidx.lifecycle.*
import com.caunb163.data.repository.RepositoryVideoImpl
import com.caunb163.mxh.state.State
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class VideoViewModel(
    private val repositoryVideoImpl: RepositoryVideoImpl
) : ViewModel() {

    val listener: LiveData<State> = liveData(Dispatchers.IO) {
        emit(State.Loading)
        try {
            repositoryVideoImpl.getAllVideo().collect {
                emit(State.Success(it))
            }
        } catch (e: Exception) {
            emit(State.Failure("${e.message}"))
        }
    }
}