package com.caunb163.domain.usecase.login

import com.caunb163.domain.model.User

interface RepositoryLogin {
    suspend fun loginWithEmailAndPassword(email: String, password: String): User?
}