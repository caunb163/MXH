package com.caunb163.domain.usecase.home.create_post

interface RepositoryCreatePost {
    suspend fun createPost(
        userId: String,
        createDate: Long,
        images: List<String>,
        content: String,
        arrCmtId: List<String>,
        arrLike: List<String>
    )
}