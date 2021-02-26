package com.caunb163.domain.usecase.home

import com.caunb163.domain.model.PostEntity

interface RepositoryHome {
    suspend fun getAllPost(): List<PostEntity>
}