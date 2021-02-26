package com.caunb163.mxh.ui.main.home

import androidx.lifecycle.*
import com.caunb163.domain.usecase.home.HomeUseCase
import com.caunb163.mxh.state.State
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel(
//    private val repositoryHomeImpl: RepositoryHomeImpl
    private val homeUseCase: HomeUseCase
) : ViewModel() {

    private val _statePost: MutableLiveData<State> = MutableLiveData()
    val statePost: LiveData<State> get() = _statePost

    fun getAllPost() {
        viewModelScope.launch {
            _statePost.value = State.Loading
            try {
                val homeUseCase = withContext(Dispatchers.IO) {
                    return@withContext homeUseCase.getAllPost()
                }

                _statePost.value = State.Success(homeUseCase)

            } catch (e: Exception) {
                e.printStackTrace()
                _statePost.value = State.Failure("${e.message}")
            }
        }
    }


//    val state: LiveData<State> = liveData(Dispatchers.IO) {
//        emit(State.Loading)
//        try {
//            repositoryHomeImpl.getAllPost().collect {
//                emit(State.Success(it))
//            }
//        } catch (e: Exception) {
//            emit(State.Failure("${e.message}"))
//        }
//    }
}