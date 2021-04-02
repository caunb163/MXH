package com.caunb163.mxh.ui.main.home

import androidx.lifecycle.*
import com.caunb163.data.repository.RepositoryHomeImpl
import com.caunb163.domain.model.PostEntity
import com.caunb163.mxh.state.State
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@InternalCoroutinesApi
class HomeViewModel(
    private val repositoryHomeImpl: RepositoryHomeImpl
) : ViewModel() {

    private val _stateLike: MutableLiveData<State> = MutableLiveData()
    val stateLike: LiveData<State> get() = _stateLike

    private val _state: MutableLiveData<State> = MutableLiveData()
    val state: LiveData<State> get() = _state

    fun getAllPost() {
        viewModelScope.launch {
            _state.value = State.Loading
            try {
                val statePost = withContext(Dispatchers.IO) {
                    return@withContext repositoryHomeImpl.getAllPost()
                }
                _state.value = State.Success(statePost)

            } catch (e: Exception) {
                _state.value = State.Failure("${e.message}")
            }
        }
    }

    val listener: LiveData<State> = liveData(Dispatchers.IO) {
        emit(State.Loading)
        try {
            repositoryHomeImpl.listenerPostChange().collect {
                emit(State.Success(it))
            }
        } catch (e: Exception) {
            emit(State.Failure("${e.message}"))
        }
    }

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

    private val _stateDelete: MutableLiveData<State> = MutableLiveData()
    val stateDelete: LiveData<State> get() = _stateDelete
    fun deletePost(post: PostEntity) {
        viewModelScope.launch {
            _stateDelete.value = State.Loading
            try {
                val delete = withContext(Dispatchers.IO) {
                    return@withContext repositoryHomeImpl.deletePost(post)
                }
                _stateDelete.value = State.Success(delete)
            } catch (e: Exception) {
                _stateDelete.value = State.Failure("${e.message}")
            }
        }
    }

    fun logout() {
        repositoryHomeImpl.logout()
    }
}