package com.caunb163.domain.model

data class MessageEntity(
    val messageId: String = "",
    val userId: String = "",
    val groupId: String = "",
    val content: String = "",
    val createDate: Long = 1614633736659,
    val image: String = "",
    val userName: String = "",
    val userAvatar: String = ""
)