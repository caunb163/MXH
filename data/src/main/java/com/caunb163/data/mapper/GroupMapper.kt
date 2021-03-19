package com.caunb163.data.mapper

import com.caunb163.domain.model.Group
import com.caunb163.domain.model.GroupEntity
import com.caunb163.domain.model.User

class GroupMapper {
    fun toEntity(
        group: Group,
        groupId: String,
        arrUser: MutableList<User>
    ): GroupEntity {
        return GroupEntity(
            name = group.name,
            groupId = groupId,
            lastMessage = group.lastMessage,
            arrUser = arrUser,
            createDate = group.createDate
        )
    }
}