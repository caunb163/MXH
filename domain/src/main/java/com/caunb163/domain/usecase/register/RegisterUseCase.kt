package com.caunb163.domain.usecase.register

import com.caunb163.domain.model.User

class RegisterUseCase(
    private val repositoryRegister: RepositoryRegister
) {
    suspend fun register(username: String, email: String, password: String, phone: String) {
        return repositoryRegister.register(username, email, password, phone)
    }
}