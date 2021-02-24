package com.caunb163.domain.usecase.home

import com.caunb163.domain.model.Post
import kotlinx.coroutines.flow.Flow

interface RepositoryHome {
    suspend fun getAllPost(): Flow<List<Post>>
}