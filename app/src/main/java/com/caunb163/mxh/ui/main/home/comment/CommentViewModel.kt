package com.caunb163.mxh.ui.main.home.comment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.caunb163.data.repository.RepositoryCommentImpl
import com.caunb163.mxh.state.State
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect

@InternalCoroutinesApi
class CommentViewModel(
    private val repositoryCommentImpl: RepositoryCommentImpl
) : ViewModel() {

    private val _postId: MutableLiveData<String> = MutableLiveData()

    val state: LiveData<State> = liveData(Dispatchers.IO) {
        emit(State.Loading)
        try {
            _postId.value?.let {
                repositoryCommentImpl.getAllComment(it).collect { list ->
                    emit(State.Success(list))
                }
            }
        } catch (e: Exception) {
            emit(State.Failure("${e.message}"))
        }
    }

    fun setPostId(postId: String) {
        _postId.value = postId
    }
}