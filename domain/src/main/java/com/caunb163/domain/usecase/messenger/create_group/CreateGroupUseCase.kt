package com.caunb163.domain.usecase.messenger.create_group

import com.caunb163.domain.model.User

class CreateGroupUseCase(
    private val repositoryCreateGroup: RepositoryCreateGroup
) {
    suspend operator fun invoke(arrUserId: List<String>, groupName: String, createDate: Long) {
        repositoryCreateGroup.createGroup(arrUserId, groupName, createDate)
    }

    suspend fun getAllUser(): List<User> {
        return repositoryCreateGroup.getAllUser()
    }
}