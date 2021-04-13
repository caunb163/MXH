package com.caunb163.domain.usecase.register

class RegisterUseCase(
    private val repositoryRegister: RepositoryRegister
) {
    suspend fun register(username: String, email: String, password: String) {
        return repositoryRegister.register(username, email, password)
    }
}