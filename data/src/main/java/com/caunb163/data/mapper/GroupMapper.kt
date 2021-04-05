package com.caunb163.data.mapper

import com.caunb163.domain.model.Group
import com.caunb163.domain.model.GroupEntity
import com.caunb163.domain.model.GroupTemp
import com.caunb163.domain.model.User

class GroupMapper {

    fun toTemp(
        group: Group,
        groupId: String
    ):GroupTemp{
        return GroupTemp(
            name = group.name,
            groupId = groupId,
            lastMessage = group.lastMessage,
            createDate = group.createDate,
            arrUserId = group.arrUserId
        )
    }

    fun toEntity(
        groupTemp: GroupTemp,
        arrUser: MutableList<User>
    ): GroupEntity {
        return GroupEntity(
            name = groupTemp.name,
            groupId = groupTemp.groupId,
            lastMessage = groupTemp.lastMessage,
            arrUser = arrUser,
            createDate = groupTemp.createDate
        )
    }
}