package com.caunb163.data.mapper

import com.caunb163.domain.model.PostEntity
import com.caunb163.domain.model.Post

class PostMapper {
    fun toEntity(
        post: Post,
        postId: String,
        username: String,
        userAvatar: String
    ): PostEntity {
        return PostEntity(
            postId = postId,
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

    fun toModel(postEntiy: PostEntity): Post {
        return Post(
            userId = postEntiy.userId,
            content = postEntiy.content,
            createDate = postEntiy.createDate,
            images = postEntiy.images,
            arrCmtId = postEntiy.arrCmtId.toMutableList(),
            arrLike = postEntiy.arrLike.toMutableList()
        )
    }

}