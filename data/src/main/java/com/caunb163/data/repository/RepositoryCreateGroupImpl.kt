package com.caunb163.data.repository

import android.util.Log
import com.caunb163.data.firebase.FB
import com.caunb163.domain.model.Group
import com.caunb163.domain.model.User
import com.caunb163.domain.usecase.messenger.create_group.RepositoryCreateGroup
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class RepositoryCreateGroupImpl : RepositoryCreateGroup {
    private val TAG = "RepositoryCreateGroupIm"
    val db = Firebase.firestore

    override suspend fun createGroup(arrUserId: List<String>, groupName: String, createDate: Long) {
        val group = Group(
            name = groupName,
            lastMessage = "Bạn đã đặt tên nhóm là $groupName",
            createDate = createDate,
            arrUserId = arrUserId.toMutableList()
        )

        db.collection(FB.GROUP).add(group).await()
    }

    override suspend fun getAllUser(): List<User> {
        val list = mutableListOf<User>()
        db.collection(FB.USER).get().addOnCompleteListener { task ->
            val data = task.result?.documents
            data?.let { d ->
                for (result in d) {
                    val user = result.toObject(User::class.java)
                    user?.let { u ->
                        list.add(u)
                    }
                }
            }
        }.await()
        Log.e(TAG, "getAllUser: ${list.size}")
        return list
    }
}