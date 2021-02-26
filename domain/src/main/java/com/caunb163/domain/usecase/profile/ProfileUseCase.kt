package com.caunb163.domain.usecase.profile

import com.caunb163.domain.model.PostEntity

class ProfileUseCase(
    private val repositoryProfile: RepositoryProfile
) {
    suspend fun getProfilePost(): List<PostEntity> {
        return repositoryProfile.getProfilePost()
    }

    suspend fun updateAvatar(uri: String): String {
        return repositoryProfile.updateAvatar(uri)
    }

    suspend fun updateBackground(uri: String): String {
        return repositoryProfile.updateBackground(uri)
    }
}