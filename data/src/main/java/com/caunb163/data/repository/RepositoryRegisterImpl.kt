package com.caunb163.data.repository

import com.caunb163.data.firebase.Auth
import com.caunb163.domain.usecase.register.RepositoryRegister

class RepositoryRegisterImpl(
    private val auth: Auth
) : RepositoryRegister {
    override suspend fun register(
        username: String,
        email: String,
        password: String
    ) {
        auth.createAccount(username, email, password)
    }
}