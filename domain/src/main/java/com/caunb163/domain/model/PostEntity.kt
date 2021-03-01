package com.caunb163.domain.model

import java.io.Serializable

data class PostEntity(
    var postId: String = "",
    val userId: String = "",
    val userName: String = "",
    var userAvatar: String = "",
    val content: String = "",
    val createDate: Long = 1614164557887,
    val images: List<String> = mutableListOf(),
    var arrCmtId: List<String> = mutableListOf(),
    var arrLike: List<String> = mutableListOf()
) : Serializable