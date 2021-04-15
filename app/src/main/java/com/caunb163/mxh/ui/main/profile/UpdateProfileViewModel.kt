package com.caunb163.mxh.ui.main.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.caunb163.data.repository.RepositoryMainImpl
import com.caunb163.data.repository.RepositoryProfileImpl
import com.caunb163.domain.model.User
import com.caunb163.mxh.state.State
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UpdateProfileViewModel(
    private val repositoryProfileImpl: RepositoryProfileImpl
) : ViewModel() {

    private val _stateUpdate: MutableLiveData<State> = MutableLiveData()
    val stateUpdate: LiveData<State> get() = _stateUpdate
    fun uploadProfile(user: User) {
        viewModelScope.launch {
            _stateUpdate.value = State.Loading
            try {
                val avatarState = withContext(Dispatchers.IO) {
                    return@withContext repositoryProfileImpl.updateProfile(user)
                }

                _stateUpdate.value = State.Success(avatarState)

            } catch (e: Exception) {
                e.printStackTrace()
                _stateUpdate.value = State.Failure("${e.message}")
            }
        }
    }
}