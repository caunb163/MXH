package com.caunb163.domain.model

data class Post(
    val userID: String = "",
    val userName: String = "",
    val userAvatar: String = "",
    val content: String = "",
    val createDate: String = "",
    val isLike: Boolean = false,
    val likeNumber: Int = 0,
    val commentNumber: Int = 0,
    val images: List<String> = mutableListOf()
)