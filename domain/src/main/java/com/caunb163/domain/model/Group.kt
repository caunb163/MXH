package com.caunb163.domain.model

data class Group(
    val name: String = "",
    val lastMessage: String = "",
    val createDate: Long = 1614570926872,
    val arrUserId: MutableList<String> = mutableListOf()
)