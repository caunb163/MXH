package com.caunb163.domain.usecase.home.create_post

class CreatePostUseCase(
    private val repositotyCreatePost: RepositoryCreatePost
) {
    suspend fun createPost(
        userId: String,
        createDate: Long,
        images: List<String>,
        content: String,
        arrCmtId: List<String>,
        arrLike: List<String>
    ) {
        repositotyCreatePost.createPost(
            userId,
            createDate,
            images,
            content,
            arrCmtId,
            arrLike
        )
    }
}