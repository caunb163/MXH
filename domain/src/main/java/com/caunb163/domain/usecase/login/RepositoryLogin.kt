package com.caunb163.domain.usecase.login

interface RepositoryLogin {
    suspend fun loginWithEmailAndPassword(email: String, password: String)
}