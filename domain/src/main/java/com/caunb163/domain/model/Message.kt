package com.caunb163.domain.model

import java.io.Serializable

data class Message(
    val userId: String = "",
    val groupId: String = "",
    val content: String = "",
    val createDate: Long = 1614633736659,
    val image: String = ""
): Serializable {
    var messageId: String = ""
}