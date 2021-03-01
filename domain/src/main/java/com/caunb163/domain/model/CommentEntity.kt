package com.caunb163.domain.model

data class CommentEntity(
    var postId: String = "",
    var username: String = "",
    var userAvatar: String = "",
    var content: String = "",
    var userId: String = "",
    var image: String = "",
    var time: Long = 1614570926872
)