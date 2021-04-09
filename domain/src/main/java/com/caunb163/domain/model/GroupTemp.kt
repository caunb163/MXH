package com.caunb163.domain.model

import java.io.Serializable

data class GroupTemp(
    val name: String = "",
    val groupId: String = "",
    val lastMessage: String = "",
    val createDate: Long = 1614570926872,
    val arrUserId: MutableList<String> = mutableListOf()
): Serializable