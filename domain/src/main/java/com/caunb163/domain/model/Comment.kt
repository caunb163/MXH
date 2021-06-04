package com.caunb163.domain.model

data class Comment(
    var postId: String = "",
    var content: String = "",
    var userId: String = "",
    var image: String = "",
    var time: Long = 1614570926872
) {
    var commentId: String = ""
}