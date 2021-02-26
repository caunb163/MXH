package com.caunb163.domain.usecase.home

import com.caunb163.domain.model.PostEntity

class HomeUseCase(
    private val repositoryHome: RepositoryHome
) {
    suspend fun getAllPost(): List<PostEntity> {
        return repositoryHome.getAllPost()
    }
}