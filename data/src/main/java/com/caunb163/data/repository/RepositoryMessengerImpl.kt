package com.caunb163.data.repository

import android.util.Log
import com.caunb163.data.firebase.FB
import com.caunb163.data.mapper.GroupMapper
import com.caunb163.domain.model.Group
import com.caunb163.domain.model.GroupEntity
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

    suspend fun getAllMyGroup(userId: String): MutableList<GroupEntity> {
        val data = db.collection(FB.GROUP).whereArrayContains("arrUserId", userId).get().await()
        val groupEntityList = mutableListOf<GroupEntity>()
        for (result in data.documents) {
            val group = result.toObject(Group::class.java)
            group?.let { g ->
                val arrUser: MutableList<User> = mutableListOf()
                for (uId in g.arrUserId) {
                    db.collection(FB.USER).document(uId).get().addOnCompleteListener { task ->
                        val user = task.result?.toObject(User::class.java)
                        user?.let { u ->
                            arrUser.add(u)
                        }
                    }.await()
                }
                val groupEntity = groupMapper.toEntity(g, result.id, arrUser)
                groupEntityList.add(groupEntity)
            }
        }
        groupEntityList.sortBy { it.createDate.dec() }
        return groupEntityList
    }

//    suspend fun listenerGroupChange(userId: String): Flow<GroupEntity> = callbackFlow {
//        val data = db.collection("Groups").whereArrayContains("arrUserId", userId)
//        val subscription = data.addSnapshotListener { value, error ->
//            if (error != null) return@addSnapshotListener
//            value?.let { qs ->
//                for (dc in qs.documentChanges) {
//                    val group = dc.document.toObject(Group::class.java)
//                    val arrUser: MutableList<User> = mutableListOf()
//                    for (uId in group.arrUserId) {
//                        db.collection("Users").document(uId).get()
//                            .addOnCompleteListener { task ->
//                                val user = task.result?.toObject(User::class.java)
//                                user?.let { u ->
//                                    arrUser.add(u)
//                                }
//                            }
//                    }
////                    val groupEntity = groupMapper.toEntity(group, dc.document.id, arrUser)
////                    when (dc.type) {
////                        DocumentChange.Type.ADDED -> {
////                            Log.e(TAG, "listenerGroupChange: ADD ${dc.document.data}")
////                            offer(groupEntity)
////                        }
////
////                        DocumentChange.Type.MODIFIED -> {
////                            Log.e(TAG, "listenerGroupChange: MODIFIED ${dc.document.data}")
////                            offer(groupEntity)
////                        }
////
////                        DocumentChange.Type.REMOVED -> {
////                            Log.e(TAG, "listenerGroupChange: REMOVED ${dc.document.data}")
////                            offer(GroupEntity(groupId = groupEntity.groupId))
////                        }
////                    }
//                }
//            }
//        }
//        awaitClose { subscription.remove() }
//    }
}