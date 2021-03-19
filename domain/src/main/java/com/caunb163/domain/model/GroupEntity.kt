package com.caunb163.domain.model

class GroupEntity(
    val name: String = "",
    val groupId: String ="",
    val lastMessage: String = "",
    val createDate: Long = 1614570926872,
    val arrUser: MutableList<User> = mutableListOf()
) {
    fun logString(): String{
        return "name: $name - groupId: $groupId - lastMessage: $lastMessage - createDate: $createDate - arrUserSize: ${arrUser.size}"
    }
}