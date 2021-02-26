package com.caunb163.domain.model

data class PostEntity(
    val userId: String = "",
    val userName: String = "",
    var userAvatar: String = "",
    val content: String = "",
    val createDate: Long = 1614164557887,
    val like: Boolean = false,
    val likeNumber: Int = 0,
    val commentNumber: Int = 0,
    val images: List<String> = mutableListOf()
)