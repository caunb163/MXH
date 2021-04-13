package com.caunb163.domain.usecase.login

interface RepositoryLogin {
    suspend fun loginWithEmailAndPassword(email: String, password: String)

    suspend fun loginWithGoogle(idToken: String)

    suspend fun getUserPhone()

    suspend fun checkPhone(verificationId: String, opt: String): Boolean

    suspend fun createUser(username: String, birthday: String, gender: String)
}