package com.caunb163.domain.usecase.register

import com.caunb163.domain.model.User

interface RepositoryRegister {
    suspend fun register(username: String, email: String, password: String, phone: String)
}