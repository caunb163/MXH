package com.caunb163.domain.usecase.home.create_post

interface RepositoryCreatePost {
    suspend fun createPost(
        userId: String,
        likeNumber: Int,
        like: Boolean,
        createDate: Long,
        images: List<String>,
        content: String,
        commentNumber: Int
    )
}