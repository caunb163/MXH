package com.caunb163.domain.usecase.login

import com.caunb163.domain.model.User

class LoginUseCase(
    private val repositoryLogin: RepositoryLogin
) {
    suspend fun loginWithEmailAndPassword(email: String, password: String): User {
        return repositoryLogin.loginWithEmailAndPassword(email, password)
    }
}