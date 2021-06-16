package com.caunb163.mxh.ui.main.profile.user

import androidx.lifecycle.*
import com.caunb163.data.repository.RepositoryHomeImpl
import com.caunb163.data.repository.RepositoryProfileImpl
import com.caunb163.mxh.state.State
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserViewModel(
    private val repositoryProfileImpl: RepositoryProfileImpl,
    private val repositoryHomeImpl: RepositoryHomeImpl
) : ViewModel() {
    private val _userId: MutableLiveData<String> = MutableLiveData()
    fun setUserId(userId: String) {
        _userId.value = userId
    }

    val listener: LiveData<State> = liveData(Dispatchers.IO) {
        emit(State.Loading)
        try {
            _userId.value?.let { uId ->
                repositoryProfileImpl.listenerPostChange(uId).collect {
                    emit(State.Success(it))
                }
            }

        } catch (e: Exception) {
            emit(State.Failure("${e.message}"))
        }
    }


    private val _stateLike: MutableLiveData<State> = MutableLiveData()
    val stateLike: LiveData<State> get() = _stateLike
    fun likePost(postId: String) {
        viewModelScope.launch {
            _stateLike.value = State.Loading
            try {
                val likepost = withContext(Dispatchers.IO) {
                    return@withContext repositoryHomeImpl.likePost(postId)
                }
                _stateLike.value = State.Success(likepost)

            } catch (e: Exception) {
                _stateLike.value = State.Failure("${e.message}")
            }
        }
    }

}