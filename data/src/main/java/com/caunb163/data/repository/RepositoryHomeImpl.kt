package com.caunb163.data.repository

import android.util.Log
import com.caunb163.data.datalocal.LocalStorage
import com.caunb163.data.mapper.PostMapper
import com.caunb163.domain.model.Post
import com.caunb163.domain.model.PostEntity
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
class RepositoryHomeImpl(
    private val postMapper: PostMapper,
    private val localStorage: LocalStorage
) {
    private val TAG = "RepositoryHomeImpl"
    private val db = Firebase.firestore
    private val user = localStorage.getAccount()!!

    suspend fun getAllPost(): MutableList<PostEntity> {
        val data = db.collection("Posts").get().await()
        val postEntityList = mutableListOf<PostEntity>()

        for (result in data.documents) {
            val post = result.toObject(Post::class.java)
            post?.let {
                db.collection("Users").document(it.userId).get().addOnCompleteListener { task ->
                    val user = task.result?.toObject(User::class.java)
                    user?.let { u ->
                        val postEntity = postMapper.toEntity(
                            post, result.id,
                            u.username, u.photoUrl
                        )
                        postEntityList.add(postEntity)
                    }
                }.await()
            }
        }
        postEntityList.sortByDescending { it.createDate.inc() }
        return postEntityList
    }

//    suspend fun getAllPost(): Flow<List<PostEntity>> = callbackFlow {
//        val data =
//            db.collection("Posts")
//
//        val subscription = data.addSnapshotListener { value, error ->
//            if (error != null) {
//                return@addSnapshotListener
//            }
//            val postEntityList = mutableListOf<PostEntity>()
//            value?.let {
//                for (result in value) {
//                    val post = result.toObject(Post::class.java)
//                    db.collection("Users").document(post.userId).get()
//                        .addOnCompleteListener { task ->
//                            val user = task.result?.toObject(User::class.java)
//                            val postEntity = postMapper.toEntity(
//                                post,
//                                result.id,
//                                user?.username ?: "",
//                                user?.photoUrl ?: ""
//                            )
//                            postEntityList.add(postEntity)
//                            postEntityList.sortByDescending { it.createDate.inc() }
//                            offer(postEntityList)
//                        }
//                }
//            }
//        }
//        awaitClose { subscription.remove() }
//    }

    suspend fun listenerPostChange(): Flow<PostEntity> = callbackFlow {
        val data =
            db.collection("Posts")
        val subscription = data.addSnapshotListener { value, error ->
            if (error != null) {
                return@addSnapshotListener
            }
            value?.let { qs ->
                for (dc in qs.documentChanges) {
                    val post = dc.document.toObject(Post::class.java)
                    db.collection("Users").document(post.userId).get()
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
                                        Log.e(TAG, "listenerPostChange: ADD ${dc.document.data}")
                                        offer(postEntity)
                                    }
                                    DocumentChange.Type.MODIFIED -> {
                                        Log.e(
                                            TAG,
                                            "listenerPostChange: MODIFIED ${dc.document.data}"
                                        )
                                        offer(postEntity)
                                    }
                                    DocumentChange.Type.REMOVED -> {
                                        Log.e(
                                            TAG,
                                            "listenerPostChange: REMOVED ${PostEntity(postId = postEntity.postId)}"
                                        )
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

    suspend fun likePost(postId: String) {
        db.collection("Posts").document(postId).get().addOnCompleteListener { task ->
            val post = task.result?.toObject(Post::class.java)
            post?.let { p ->
                val arrLike = p.arrLike
                if (arrLike.contains(user.userId)) {
                    arrLike.remove(user.userId)
                } else {
                    arrLike.add(user.userId)
                }
                db.collection("Posts").document(postId).update("arrLike", arrLike)
            }
        }.await()
    }
}

