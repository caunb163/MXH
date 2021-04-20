package com.caunb163.domain.usecase.home.create_post

interface RepositoryCreatePost {
    suspend fun createPost(
        userId: String,
        createDate: Long,
        images: List<String>,
        content: String,
        arrCmtId: MutableList<String>,
        arrLike: MutableList<String>,
        video: String
    )
}