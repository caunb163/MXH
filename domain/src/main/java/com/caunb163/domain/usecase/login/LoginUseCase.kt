package com.caunb163.domain.usecase.login

class LoginUseCase(
    private val repositoryLogin: RepositoryLogin
) {
    suspend fun loginWithEmailAndPassword(email: String, password: String) {
        return repositoryLogin.loginWithEmailAndPassword(email, password)
    }

    suspend fun loginWithGoogle(idToken: String) {
        repositoryLogin.loginWithGoogle(idToken)
    }

    suspend fun getUserPhone() {
        repositoryLogin.getUserPhone()
    }

    suspend fun checkPhone(verificationId: String, opt: String): Boolean {
        return repositoryLogin.checkPhone(verificationId, opt)
    }

    suspend fun createUser(username: String, birthday: String, gender: String) {
        repositoryLogin.createUser(username, birthday, gender)
    }
}