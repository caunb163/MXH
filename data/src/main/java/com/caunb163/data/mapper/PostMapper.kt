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
            postId = post.postId,
            userId = post.userId,
            userName = username,
            userAvatar = userAvatar,
            content = post.content,
            createDate = post.createDate,
            images = post.images,
            arrCmtId = post.arrCmtId,
            arrLike = post.arrLike
        )
    }

}