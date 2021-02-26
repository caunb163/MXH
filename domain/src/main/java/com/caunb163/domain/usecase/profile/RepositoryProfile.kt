package com.caunb163.domain.usecase.profile

import com.caunb163.domain.model.Post
import com.caunb163.domain.model.PostEntity

interface RepositoryProfile {
    suspend fun getProfilePost(): List<PostEntity>

    suspend fun updateAvatar(uri: String)

    suspend fun updateBackground(uri: String)
}