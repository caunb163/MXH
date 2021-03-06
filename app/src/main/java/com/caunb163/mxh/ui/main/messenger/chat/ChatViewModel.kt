package com.caunb163.mxh.ui.main.messenger.chat

import androidx.lifecycle.*
import com.caunb163.data.repository.RepositoryChatImpl
import com.caunb163.mxh.state.State
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChatViewModel(
    private val repositoryChatImpl: RepositoryChatImpl
) : ViewModel() {
    private val TAG = "ChatViewModel"

    private val _groupId: MutableLiveData<String> = MutableLiveData()

    fun setGroupId(groupId: String) {
        _groupId.value = groupId
    }

    val listener: LiveData<State> = liveData(Dispatchers.IO) {
        emit(State.Loading)
        try {
            repositoryChatImpl.listenerMessageChange(_groupId.value!!).collect {
                emit(State.Success(it))
            }
        } catch (e: Exception) {
            emit(State.Failure("${e.message}"))
        }
    }

    private val _stateCreateMessage: MutableLiveData<State> = MutableLiveData()
    val stateCreateMessage: LiveData<State> get() = _stateCreateMessage
    fun createMessage(
        content: String,
        createDate: Long,
        userId: String,
        image: String
    ) {
        viewModelScope.launch {
            _stateCreateMessage.value = State.Loading
            try {
                val stateM = withContext(Dispatchers.IO) {
                    return@withContext repositoryChatImpl.createMessenger(
                        content, createDate, _groupId.value!!, userId, image
                    )
                }
                _stateCreateMessage.value = State.Success(stateM)
            } catch (e: Exception) {
                _stateCreateMessage.value = State.Failure("${e.message}")
            }
        }
    }


}