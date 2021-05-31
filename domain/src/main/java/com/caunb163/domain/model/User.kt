package com.caunb163.domain.model

import java.io.Serializable

data class User(
    var username: String = "",
    var email: String = "",
    var photoUrl: String = "",
    var arrPostId: MutableList<String> = mutableListOf(),
    var userId: String = "",
    var phone: String = "",
    var photoBackground: String = "",
    var birthDay: String = "",
    var gender: String = "",
    var intro: String = "",
    var ads: Boolean = false,
    var admin: Boolean = false
) : Serializable {
    var ischeck: Boolean = false
}