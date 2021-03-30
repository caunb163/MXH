package com.caunb163.domain.model

data class User(
    var username: String = "",
    var email: String = "",
    var photoUrl: String = "",
    var arrPostId: MutableList<String> = mutableListOf(),
    var userId: String = "",
    var phone: String = "",
    var photoBackground: String = "",
    var birthDay: String = "",
    var address: String = "",
    var intro: String = ""
) {
    var ischeck: Boolean = false
}