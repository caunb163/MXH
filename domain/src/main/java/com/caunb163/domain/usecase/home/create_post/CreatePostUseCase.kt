package com.caunb163.domain.usecase.home.create_post

class CreatePostUseCase(
    private val repositotyCreatePost: RepositoryCreatePost
) {
    suspend fun createPost(userId: String, createDate: Long, images: List<String>, content: String, arrCmtId: MutableList<String>, arrLike: MutableList<String>, video: String, isAds: Boolean) {
        repositotyCreatePost.createPost(userId, createDate, images, content, arrCmtId, arrLike, video, isAds)
    }
}