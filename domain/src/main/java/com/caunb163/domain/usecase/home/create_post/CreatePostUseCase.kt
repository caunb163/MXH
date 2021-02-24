package com.caunb163.domain.usecase.home.create_post

class CreatePostUseCase(
    private val repositotyCreatePost: RepositoryCreatePost
) {
    suspend fun createPost(
        userId: String,
        userName: String,
        userAvatar: String,
        likeNumber: Int,
        like: Boolean,
        createDate: Long,
        images: List<String>,
        content: String,
        commentNumber: Int
    ) {
        repositotyCreatePost.createPost(
            userId,
            userName,
            userAvatar,
            likeNumber,
            like,
            createDate,
            images,
            content,
            commentNumber
        )
    }
}