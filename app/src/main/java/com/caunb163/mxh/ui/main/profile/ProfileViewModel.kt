package com.caunb163.mxh.ui.main.profile

import androidx.lifecycle.*
import com.caunb163.data.repository.RepositoryHomeImpl
import com.caunb163.data.repository.RepositoryMainImpl
import com.caunb163.data.repository.RepositoryProfileImpl
import com.caunb163.domain.model.Post
import com.caunb163.mxh.state.State
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfileViewModel(
    private val repositoryProfileImpl: RepositoryProfileImpl,
    private val repositoryMainImpl: RepositoryMainImpl,
    private val repositoryHomeImpl: RepositoryHomeImpl
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

    val user: LiveData<State> = liveData(Dispatchers.IO) {
        emit(State.Loading)
        try {
            repositoryMainImpl.saveUser().collect {
                emit(State.Success(it))
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

    private val _stateDelete: MutableLiveData<State> = MutableLiveData()
    val stateDelete: LiveData<State> get() = _stateDelete
    fun deletePost(post: Post) {
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
}