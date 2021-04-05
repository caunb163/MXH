package com.caunb163.data.mapper

import com.caunb163.domain.model.Message
import com.caunb163.domain.model.MessageEntity

class MessageMapper {
    fun toEntity(
        message: Message,
        userName: String,
        userAvatar: String,
        messageId: String,
    ): MessageEntity {
        return MessageEntity(
            messageId = messageId,
            userId = message.userId,
            groupId = message.groupId,
            content = message.content,
            createDate = message.createDate,
            image = message.image,
            userName = userName,
            userAvatar = userAvatar
        )
    }
}