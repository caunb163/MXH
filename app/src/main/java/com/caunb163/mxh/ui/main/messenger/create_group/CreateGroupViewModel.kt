package com.caunb163.mxh.ui.main.messenger.create_group

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.caunb163.domain.usecase.messenger.create_group.CreateGroupUseCase
import com.caunb163.mxh.state.State
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CreateGroupViewModel(
    private val createGroupUseCase: CreateGroupUseCase
) : ViewModel() {

    private val _stateUser: MutableLiveData<State> = MutableLiveData()
    val stateUser: LiveData<State> get() = _stateUser

    fun getAllUser() {
        viewModelScope.launch {
            _stateUser.value = State.Loading
            try {
                val stateListUser = withContext(Dispatchers.IO) {
                    return@withContext createGroupUseCase.getAllUser()
                }
                _stateUser.value = State.Success(stateListUser)
            } catch (e: Exception) {
                _stateUser.value = State.Failure("${e.message}")
            }
        }
    }

    private val _stateGroup: MutableLiveData<State> = MutableLiveData()
    val stateGroup: LiveData<State> get() = _stateGroup

    fun createGroup(arrUserId: List<String>, groupName: String, createDate: Long) {
        viewModelScope.launch {
            _stateGroup.value = State.Loading
            try {
                val stateCreateGroup = withContext(Dispatchers.IO) {
                    return@withContext createGroupUseCase(arrUserId, groupName, createDate)
                }
                _stateGroup.value = State.Success(stateCreateGroup)
            } catch (e: Exception) {
                _stateGroup.value = State.Failure("${e.message}")
            }
        }
    }

}