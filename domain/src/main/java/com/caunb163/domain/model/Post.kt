package com.caunb163.domain.model

import java.io.Serializable

data class Post(
    var userId: String = "",
    val content: String = "",
    val createDate: Long = 1614164557887,
    val images: List<String> = mutableListOf(),
    var arrCmtId: MutableList<String> = mutableListOf(),
    var arrLike: MutableList<String> = mutableListOf(),
    var video: String = ""
) : Serializable {
    var postId: String = ""
    var remove: Boolean = false
    var isAds: Boolean = false
}