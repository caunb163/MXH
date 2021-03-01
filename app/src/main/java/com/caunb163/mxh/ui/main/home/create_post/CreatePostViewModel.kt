package com.caunb163.mxh.ui.main.home.create_post

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.caunb163.domain.usecase.home.create_post.CreatePostUseCase
import com.caunb163.mxh.state.State
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CreatePostViewModel(
    private val createPostUseCase: CreatePostUseCase
) : ViewModel() {

    private val _state: MutableLiveData<State> = MutableLiveData()
    val state: LiveData<State> get() = _state

    fun createPost(
        userId: String,
        createDate: Long,
        images: List<String>,
        content: String
    ) {
        viewModelScope.launch {
            _state.value = State.Loading
            try {
                val createPostUseCase = withContext(Dispatchers.IO) {
                    return@withContext createPostUseCase.createPost(
                        userId,
                        createDate,
                        images,
                        content,
                        mutableListOf(),
                        mutableListOf()
                    )
                }
                _state.value = State.Success(createPostUseCase)

            } catch (e: Exception) {
                e.printStackTrace()
                _state.value = State.Failure("${e.message}")
            }
        }
    }
}