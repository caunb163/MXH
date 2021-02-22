package com.caunb163.domain.usecase.home

import com.caunb163.domain.model.Post

interface RepositoryHome {
    suspend fun getAllPost(): List<Post>
}