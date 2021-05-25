package com.caunb163.mxh.ui.main.home.create_post

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.caunb163.data.datalocal.ProfanityWord
import com.caunb163.data.repository.WordRepositoty
import com.caunb163.domain.usecase.home.create_post.CreatePostUseCase
import com.caunb163.mxh.state.State
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CreatePostViewModel(
    private val createPostUseCase: CreatePostUseCase,
    private val context: Context
) : ViewModel() {

    private val wordRepositoty = WordRepositoty(context)

    private val _state: MutableLiveData<State> = MutableLiveData()
    val state: LiveData<State> get() = _state

    fun createPost(
        userId: String,
        createDate: Long,
        images: List<String>,
        content: String,
        video: String,
        isAds: Boolean
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
                        mutableListOf(),
                        video,
                        isAds
                    )
                }
                _state.value = State.Success(createPostUseCase)

            } catch (e: Exception) {
                e.printStackTrace()
                _state.value = State.Failure("${e.message}")
            }
        }
    }

    private val _listWord: MutableLiveData<MutableList<ProfanityWord>> = MutableLiveData()
    val listWord: LiveData<MutableList<ProfanityWord>> get() = _listWord
    fun getAllWord() {
        viewModelScope.launch {
            wordRepositoty.getProfanityWord().let {
                if (it.isNotEmpty()){
                    _listWord.value = it.toMutableList()
                }
            }
        }
    }
}