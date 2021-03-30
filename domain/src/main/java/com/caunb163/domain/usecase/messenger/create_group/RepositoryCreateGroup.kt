package com.caunb163.domain.usecase.messenger.create_group

import com.caunb163.domain.model.User

interface RepositoryCreateGroup {
    suspend fun createGroup(arrUserId: List<String>, groupName: String, createDate: Long)

    suspend fun getAllUser(): List<User>
}