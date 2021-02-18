package com.caunb163.mxh.state

sealed class State {
    object Loading : State()
    data class Failure(val message: String) : State()
    data class Success<T>(val data: T) : State()
}
