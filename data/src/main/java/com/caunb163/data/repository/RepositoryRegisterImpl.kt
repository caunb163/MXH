package com.caunb163.data.repository

import com.caunb163.data.firebase.Auth
import com.caunb163.domain.model.User
import com.caunb163.domain.usecase.register.RepositoryRegister

class RepositoryRegisterImpl(
    private val auth: Auth
) : RepositoryRegister {
    override suspend fun register(
        username: String,
        email: String,
        password: String,
        phone: String
    ): User {
        val result = auth.createAccount(username, email, password, phone)
        return User(
            username = result.displayName ?: "",
            email = result.email ?: "",
            photoUrl = result.photoUrl?.toString() ?: "",
            arrPostId = mutableListOf(),
            userId = result.uid
        )
    }
}