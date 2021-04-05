package com.caunb163.mxh.ui.main.profile

import androidx.lifecycle.*
import com.caunb163.data.repository.RepositoryProfileImpl
import com.caunb163.mxh.state.State
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfileViewModel(
    private val repositoryProfileImpl: RepositoryProfileImpl
) : ViewModel() {
    val listener: LiveData<State> = liveData(Dispatchers.IO) {
        emit(State.Loading)
        try {
            repositoryProfileImpl.listenerPostChange().collect {
                emit(State.Success(it))
            }
        } catch (e: Exception) {
            emit(State.Failure("${e.message}"))
        }
    }

    private val _stateAvatar: MutableLiveData<State> = MutableLiveData()
    val stateAvatar: LiveData<State> get() = _stateAvatar
    fun uploadAvatar(uri: String) {
        viewModelScope.launch {
            _stateAvatar.value = State.Loading
            try {
                val avatarState = withContext(Dispatchers.IO) {
                    return@withContext repositoryProfileImpl.updateAvatar(uri)
                }

                _stateAvatar.value = State.Success(avatarState)

            } catch (e: Exception) {
                e.printStackTrace()
                _stateAvatar.value = State.Failure("${e.message}")
            }
        }
    }

    private val _stateBackground: MutableLiveData<State> = MutableLiveData()
    val stateBackground: LiveData<State> get() = _stateBackground
    fun uploadBackground(uri: String) {
        viewModelScope.launch {
            _stateBackground.value = State.Loading
            try {
                val avatarState = withContext(Dispatchers.IO) {
                    return@withContext repositoryProfileImpl.updateBackground(uri)
                }

                _stateBackground.value = State.Success(avatarState)

            } catch (e: Exception) {
                e.printStackTrace()
                _stateBackground.value = State.Failure("${e.message}")
            }
        }
    }
}