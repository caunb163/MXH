package com.caunb163.domain.model

data class Post(
    var postId: String = "",
    val userId: String = "",
    val content: String = "",
    val createDate: Long = 1614164557887,
    val images: List<String> = mutableListOf(),
    var arrCmtId: MutableList<String> = mutableListOf(),
    var arrLike: MutableList<String> = mutableListOf()
)