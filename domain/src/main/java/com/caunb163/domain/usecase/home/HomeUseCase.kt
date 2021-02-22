package com.caunb163.domain.usecase.home

import com.caunb163.domain.model.Post

class HomeUseCase(
    private val repositoryHome: RepositoryHome
) {
    suspend fun getAllPost(): List<Post> {
        return repositoryHome.getAllPost()
    }
}