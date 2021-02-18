package com.caunb163.data.repository

import com.caunb163.data.firebase.Auth
import com.caunb163.domain.model.User
import com.caunb163.domain.usecase.login.RepositoryLogin

class RepositoryLoginImpl(
    private val auth: Auth
) : RepositoryLogin {
    override suspend fun loginWithEmailAndPassword(email: String, password: String): User {
        val result = auth.authWithEmailAndPassword(email, password)
        return User(
            result.uid,
            result.displayName ?: "",
            result.email ?: "",
            result.photoUrl?.toString() ?: ""
        )
    }
}