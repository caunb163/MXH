package com.caunb163.mxh.ui.main.home

import com.caunb163.domain.model.PostEntity

interface HomeOnClick {
    fun createPostClick()

    fun onCommentClick(post: PostEntity)

    fun onLikeClick(postId: String)

    fun onShareClick(content: String)
}