package com.caunb163.data.repository

import android.util.Log
import com.caunb163.data.mapper.GroupMapper
import com.caunb163.domain.model.Group
import com.caunb163.domain.model.GroupEntity
import com.caunb163.domain.model.GroupTemp
import com.caunb163.domain.model.User
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

@ExperimentalCoroutinesApi
class RepositoryMessengerImpl(
    private val groupMapper: GroupMapper
) {
    private val TAG = "RepositoryMessengerImpl"
    private val db = Firebase.firestore

    suspend fun listenerGroupChange(userId: String): Flow<GroupTemp> = callbackFlow {
        val data = db.collection("Groups").whereArrayContains("arrUserId", userId)
        val subscription = data.addSnapshotListener { value, error ->
            if (error != null) return@addSnapshotListener
            value?.let { qs ->
                for (dc in qs.documentChanges) {
                    val group = dc.document.toObject(Group::class.java)
                    val arrUser: MutableList<User> = mutableListOf()
                    for (uId in group.arrUserId) {
                        db.collection("Users").document(uId).get()
                            .addOnCompleteListener { task ->
                                val user = task.result?.toObject(User::class.java)
                                user?.let { u ->
                                    arrUser.add(u)
                                }
                            }
                    }
                    val groupTemp = groupMapper.toTemp(group, dc.document.id)
                    when (dc.type) {
                        DocumentChange.Type.ADDED -> {
                            Log.e(TAG, "listenerGroupChange: ADD ${dc.document.data}")
                            offer(groupTemp)
                        }

                        DocumentChange.Type.MODIFIED -> {
                            Log.e(TAG, "listenerGroupChange: MODIFIED ${dc.document.data}")
                            offer(groupTemp)
                        }

                        DocumentChange.Type.REMOVED -> {
                            Log.e(TAG, "listenerGroupChange: REMOVED ${dc.document.data}")
                            offer(GroupTemp(groupId = groupTemp.groupId))
                        }
                    }
                }
            }
        }
        awaitClose { subscription.remove() }
    }

    suspend fun getUserGroup(groupTemp: GroupTemp): GroupEntity {
        val arrUser: MutableList<User> = mutableListOf()
        for (userId in groupTemp.arrUserId) {
            db.collection("Users").document(userId).get()
                .addOnCompleteListener { task ->
                    val user = task.result?.toObject(User::class.java)
                    user?.let { u ->
                        arrUser.add(u)
                    }
                }.await()
        }
        return groupMapper.toEntity(groupTemp, arrUser)
    }

}