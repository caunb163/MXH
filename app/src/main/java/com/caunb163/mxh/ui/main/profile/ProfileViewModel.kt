package com.caunb163.mxh.ui.main.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.caunb163.domain.usecase.profile.ProfileUseCase
import com.caunb163.mxh.state.State
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfileViewModel(
    private val profileUseCase: ProfileUseCase
) : ViewModel() {
    private val _statePost: MutableLiveData<State> = MutableLiveData()
    val statePost: LiveData<State> get() = _statePost

    private val _stateAvatar: MutableLiveData<State> = MutableLiveData()
    val stateAvatar: LiveData<State> get() = _stateAvatar

    private val _stateBackground: MutableLiveData<State> = MutableLiveData()
    val stateBackground: LiveData<State> get() = _stateBackground

    fun getProfilePost() {
        viewModelScope.launch {
            _statePost.value = State.Loading
            try {
                val profileState = withContext(Dispatchers.IO) {
                    return@withContext profileUseCase.getProfilePost()
                }

                _statePost.value = State.Success(profileState)

            } catch (e: Exception) {
                e.printStackTrace()
                _statePost.value = State.Failure("${e.message}")
            }
        }
    }

    fun uploadAvatar(uri: String) {
        viewModelScope.launch {
            _stateAvatar.value = State.Loading
            try {
                val avatarState = withContext(Dispatchers.IO) {
                    return@withContext profileUseCase.updateAvatar(uri)
                }

                _stateAvatar.value = State.Success(avatarState)

            } catch (e: Exception) {
                e.printStackTrace()
                _stateAvatar.value = State.Failure("${e.message}")
            }
        }
    }

    fun uploadBackground(uri: String) {
        viewModelScope.launch {
            _stateBackground.value = State.Loading
            try {
                val avatarState = withContext(Dispatchers.IO) {
                    return@withContext profileUseCase.updateBackground(uri)
                }

                _stateBackground.value = State.Success(avatarState)

            } catch (e: Exception) {
                e.printStackTrace()
                _stateBackground.value = State.Failure("${e.message}")
            }
        }
    }
}