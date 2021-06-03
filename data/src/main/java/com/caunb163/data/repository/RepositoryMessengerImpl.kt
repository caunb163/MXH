package com.caunb163.data.repository

import android.util.Log
import com.caunb163.data.firebase.FireStore
import com.caunb163.domain.model.Group
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

@ExperimentalCoroutinesApi
class RepositoryMessengerImpl {
    private val TAG = "RepositoryMessengerImpl"
    private val db = Firebase.firestore

    suspend fun listenerGroupChange(userId: String): Flow<Group> = callbackFlow {
        val data = db.collection(FireStore.GROUP).whereArrayContains("arrUserId", userId).orderBy("createDate", Query.Direction.DESCENDING)
        val subscription = data.addSnapshotListener { value, error ->
            if (error != null) return@addSnapshotListener
            value?.let { qs ->
                for (dc in qs.documentChanges) {
                    val group = dc.document.toObject(Group::class.java)
                    group.groupId = dc.document.id
                    Log.e(TAG, "listener ${group.groupId}:" )
                    when (dc.type) {
                        DocumentChange.Type.ADDED -> {
                            offer(group)
                        }

                        DocumentChange.Type.MODIFIED -> {
                            offer(group)
                        }

                        DocumentChange.Type.REMOVED -> {
                            val g = Group()
                            g.groupId = group.groupId
                            offer(g)
                        }
                    }
                }
            }
        }
        awaitClose { subscription.remove() }
    }
}