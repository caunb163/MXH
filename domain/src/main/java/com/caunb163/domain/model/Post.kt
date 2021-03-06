package com.caunb163.domain.model

import java.io.Serializable

data class Post(
    var userId: String = "",
    val content: String = "",
    val createDate: Long = 1614164557887,
    val images: List<String> = mutableListOf(),
    var arrCmtId: MutableList<String> = mutableListOf(),
    var arrLike: MutableList<String> = mutableListOf(),
    var video: String = "",
    var active: Boolean = true
) : Serializable {
    var postId: String = ""
    var isAds: Boolean = false
}