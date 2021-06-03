package com.caunb163.data.repository

import com.caunb163.data.firebase.FireStore
import com.caunb163.domain.model.*
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class RepositoryVideoImpl {
    private val TAG = "RepositoryVideoImpl"
    private val db = Firebase.firestore

    @ExperimentalCoroutinesApi
    suspend fun getAllVideo(): Flow<Post> = callbackFlow {
        val data = db.collection(FireStore.POST).whereNotEqualTo("video", "")
        val subscription = data.addSnapshotListener { value, error ->
            if (error != null) return@addSnapshotListener
            value?.let { qs ->
                val list =
                    qs.documentChanges.sortedByDescending { it.document.toObject(Post::class.java).createDate.inc() }
                for (dc in list) {
                    val post = dc.document.toObject(Post::class.java)
                    post.postId = dc.document.id
                    when (dc.type) {
                        DocumentChange.Type.ADDED -> {
                            offer(post)
                        }
                        DocumentChange.Type.MODIFIED -> {
                            offer(post)
                        }
                        DocumentChange.Type.REMOVED -> {
                            val p = Post()
                            p.postId = dc.document.id
                            offer(p)
                        }
                    }
                }
            }
        }
        awaitClose { subscription.remove() }
    }
}
