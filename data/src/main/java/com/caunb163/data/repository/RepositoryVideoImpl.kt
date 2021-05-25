package com.caunb163.data.repository

import android.util.Log
import com.caunb163.data.firebase.FireStore
import com.caunb163.data.mapper.PostMapper
import com.caunb163.domain.model.*
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class RepositoryVideoImpl(
    private val postMapper: PostMapper
) {
    private val TAG = "RepositoryVideoImpl"
    private val db = Firebase.firestore

    @ExperimentalCoroutinesApi
    suspend fun getAllVideo(): Flow<PostEntity> = callbackFlow {
        val data = db.collection(FireStore.POST).whereNotEqualTo("video", "")
        val subscription = data.addSnapshotListener { value, error ->
            if (error != null) return@addSnapshotListener
            value?.let { qs ->
                val list =
                    qs.documentChanges.sortedByDescending { it.document.toObject(Post::class.java).createDate }
                for (dc in list) {
                    val post = dc.document.toObject(Post::class.java)
                    db.collection(FireStore.USER).document(post.userId).get()
                        .addOnCompleteListener { task ->
                            val user = task.result?.toObject(User::class.java)
                            user?.let { u ->
                                val postEntity = postMapper.toEntity(
                                    post,
                                    dc.document.id,
                                    u.username,
                                    u.photoUrl
                                )
                                when (dc.type) {
                                    DocumentChange.Type.ADDED -> {
                                        Log.e(TAG, "getAllVideo: ADDED $postEntity")
                                        offer(postEntity)
                                    }
                                    DocumentChange.Type.MODIFIED -> {
                                        Log.e(TAG, "getAllVideo: MODIFIED $postEntity")
                                        offer(postEntity)
                                    }
                                    DocumentChange.Type.REMOVED -> {
                                        offer(PostEntity(postId = postEntity.postId))
                                    }
                                }
                            }
                        }
                }
            }
        }
        awaitClose { subscription.remove() }
    }

    suspend fun getVideoTest(): MutableList<PostEntity> {
        val list = mutableListOf<PostEntity>()
        val data = db.collection(FireStore.POST).whereNotEqualTo("video", "").get().await()
        for (item in data.toMutableList()) {
            val post = item.toObject(Post::class.java)
            db.collection(FireStore.USER).document(post.userId).get()
                .addOnCompleteListener { task ->
                    val user = task.result?.toObject(User::class.java)
                    user?.let { u ->
                        val postEntity = postMapper.toEntity(
                            post,
                            item.id,
                            u.username,
                            u.photoUrl
                        )
                        list.add(postEntity)
                    }
                }.await()

        }
        Log.e(TAG, "getVideoTest: ${list.size}" )
        return list
    }
}
