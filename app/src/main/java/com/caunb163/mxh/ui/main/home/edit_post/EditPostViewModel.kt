package com.caunb163.mxh.ui.main.home.edit_post

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.caunb163.data.repository.RepositoryEditPostImpl
import com.caunb163.domain.model.PostEntity
import com.caunb163.mxh.state.State
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EditPostViewModel(
    private val repositoryEditPostImpl: RepositoryEditPostImpl
): ViewModel() {

    private val _stateEdit: MutableLiveData<State> = MutableLiveData()
    val stateEdit: LiveData<State> get() = _stateEdit
    fun editPost(postEntity: PostEntity) {
        viewModelScope.launch {
            _stateEdit.value = State.Loading
            try {
                val edit = withContext(Dispatchers.IO) {
                    return@withContext repositoryEditPostImpl.editPost(postEntity)
                }
                _stateEdit.value = State.Success(edit)
            } catch (e: Exception) {
                _stateEdit.value = State.Failure("${e.message}")
            }
        }
    }
}