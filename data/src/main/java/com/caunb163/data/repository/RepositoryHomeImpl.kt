package com.caunb163.data.repository

import com.caunb163.data.mapper.PostMapper
import com.caunb163.domain.model.Post
import com.caunb163.domain.model.PostEntity
import com.caunb163.domain.model.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

@ExperimentalCoroutinesApi
class RepositoryHomeImpl(
    private val postMapper: PostMapper
) {
    private val TAG = "RepositoryHomeImpl"
    private val db = Firebase.firestore

    suspend fun getAllPost(): Flow<List<PostEntity>> = callbackFlow {
        val data =
            db.collection("Posts")

        val subscription = data.addSnapshotListener { value, error ->
            if (error != null) {
                return@addSnapshotListener
            }
            val postEntityList = mutableListOf<PostEntity>()

            value?.let {
                for (result in value) {
                    val post = result.toObject(Post::class.java)
                    db.collection("Users").document(post.userId).get()
                        .addOnCompleteListener { task ->
                            val user = task.result?.toObject(User::class.java)
                            val postEntity = postMapper.toEntity(
                                post,
                                user?.username ?: "",
                                user?.photoUrl ?: ""
                            )
                            postEntityList.add(postEntity)
                            postEntityList.sortByDescending { it.createDate.inc() }
                            offer(postEntityList)
                        }
                }
            }
        }
        awaitClose { subscription.remove() }
    }
}

