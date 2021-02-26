package com.caunb163.data.mapper

import com.caunb163.domain.model.PostEntity
import com.caunb163.domain.model.Post

class PostMapper {
    fun toEntity(
        post: Post,
        username: String,
        userAvatar: String
    ): PostEntity {
        return PostEntity(
            userId = post.userId,
            userName = username,
            userAvatar = userAvatar,
            content = post.content,
            createDate = post.createDate,
            like = post.like,
            likeNumber = post.likeNumber,
            commentNumber = post.commentNumber,
            images = post.images
        )
    }

}