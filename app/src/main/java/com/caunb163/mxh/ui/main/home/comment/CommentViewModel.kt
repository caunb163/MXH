package com.caunb163.mxh.ui.main.home.comment

import androidx.lifecycle.*
import com.caunb163.data.repository.RepositoryCommentImpl
import com.caunb163.mxh.state.State
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@InternalCoroutinesApi
class CommentViewModel(
    private val repositoryCommentImpl: RepositoryCommentImpl
) : ViewModel() {

    private val _postId: MutableLiveData<String> = MutableLiveData()
    fun setPostId(postId: String) {
        _postId.value = postId
    }

    val state: LiveData<State> = liveData(Dispatchers.IO) {
        emit(State.Loading)
        try {
            _postId.value?.let {
                repositoryCommentImpl.listenerComment(it).collect { comment ->
                    emit(State.Success(comment))
                }
            }
        } catch (e: Exception) {
            emit(State.Failure("${e.message}"))
        }
    }

    private val _stateCmt: MutableLiveData<State> = MutableLiveData()
    val stateCmt: LiveData<State> get() = _stateCmt

    fun createComment(
        userId: String,
        time: Long,
        postId: String,
        image: String,
        content: String
    ) {
        viewModelScope.launch {
            _stateCmt.value = State.Loading
            try {
                val createComment = withContext(Dispatchers.IO) {
                    return@withContext repositoryCommentImpl.createComment(
                        userId,
                        time,
                        postId,
                        image,
                        content
                    )
                }
                _stateCmt.value = State.Success(createComment)

            } catch (e: Exception) {
                e.printStackTrace()
                _stateCmt.value = State.Failure("${e.message}")
            }
        }
    }

}