package com.caunb163.data.mapper

import com.caunb163.domain.model.Comment
import com.caunb163.domain.model.CommentEntity

class CommentMapper {
    fun toEntity(
        comment: Comment,
        username: String,
        userAvatar: String
    ): CommentEntity {
        return CommentEntity(
            userId = comment.userId,
            username = username,
            userAvatar = userAvatar,
            content = comment.content,
            image = comment.image,
            postId = comment.postId,
            time = comment.time
        )
    }

}