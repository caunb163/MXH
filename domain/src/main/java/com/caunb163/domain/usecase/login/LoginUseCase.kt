package com.caunb163.domain.usecase.login

class LoginUseCase(
    private val repositoryLogin: RepositoryLogin
) {
    suspend fun loginWithEmailAndPassword(email: String, password: String) {
        return repositoryLogin.loginWithEmailAndPassword(email, password)
    }
}