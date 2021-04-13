package com.caunb163.domain.usecase.register

interface RepositoryRegister {
    suspend fun register(username: String, email: String, password: String)
}