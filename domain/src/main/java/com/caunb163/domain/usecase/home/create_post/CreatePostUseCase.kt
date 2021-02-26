package com.caunb163.domain.usecase.home.create_post

class CreatePostUseCase(
    private val repositotyCreatePost: RepositoryCreatePost
) {
    suspend fun createPost(
        userId: String,
        likeNumber: Int,
        like: Boolean,
        createDate: Long,
        images: List<String>,
        content: String,
        commentNumber: Int
    ) {
        repositotyCreatePost.createPost(
            userId,
            likeNumber,
            like,
            createDate,
            images,
            content,
            commentNumber
        )
    }
}