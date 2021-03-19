package com.caunb163.domain.model

data class Message(
    val userId: String = "",
    val groupId: String = "",
    val content: String = "",
    val createDate: Long = 1614633736659
)