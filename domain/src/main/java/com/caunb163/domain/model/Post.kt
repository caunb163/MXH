package com.caunb163.domain.model

data class Post(
    var postId: String = "",
    val userId: String = "",
    val content: String = "",
    val createDate: Long = 1614164557887,
    val images: List<String> = mutableListOf(),
    var arrCmtId: List<String> = mutableListOf(),
    var arrLike: List<String> = mutableListOf()
)