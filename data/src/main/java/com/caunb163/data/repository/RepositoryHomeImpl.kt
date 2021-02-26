package com.caunb163.data.repository

import com.caunb163.data.mapper.PostMapper
import com.caunb163.domain.model.Post
import com.caunb163.domain.model.PostEntity
import com.caunb163.domain.model.User
import com.caunb163.domain.usecase.home.RepositoryHome
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class RepositoryHomeImpl(
    private val postMapper: PostMapper
) : RepositoryHome {
    private val TAG = "RepositoryHomeImpl"
    private val db = Firebase.firestore

    override suspend fun getAllPost(): List<PostEntity> {
        val listPost = mutableListOf<Post>()
        val listEntity = mutableListOf<PostEntity>()

        val data = db.collection("Posts").get().await()
        listPost.addAll(data.toMutableList().map {
            it.toObject(Post::class.java)
        })
        for (post in listPost) {
            db.collection("Users").document(post.userId).get().addOnCompleteListener { task ->
                val user = task.result?.toObject(User::class.java)
                val postEntity =
                    postMapper.toEntity(post, user?.username ?: "", user?.photoUrl ?: "")
                listEntity.add(postEntity)
            }.await()
        }
        listEntity.sortByDescending { it.createDate.inc() }
        return listEntity
    }

//    @ExperimentalCoroutinesApi
//    suspend fun getAllPost(): Flow<List<PostEntity>> = callbackFlow {
//        val db = Firebase.firestore
//        val data =
//            db.collection("Posts").orderBy("createDate", Query.Direction.DESCENDING)
//
//        val subscription = data.addSnapshotListener { value, error ->
//            if (error != null) {
//                return@addSnapshotListener
//            }
//            val postEntityList = mutableListOf<PostEntity>()
//            value?.let {
//                for (result in value) {
//                    val post = result.toObject(Post::class.java)
//                    Log.e(TAG, "getAllPost: $post" )
//                    db.collection("Users").document(post.userId).get()
//                        .addOnCompleteListener { task ->
//                            val user = task.result?.toObject(User::class.java)
//                            val postEntity = postMapper.toEntity(
//                                post,
//                                user?.username ?: "",
//                                user?.photoUrl ?: ""
//                            )
//                            Log.e(TAG, "getAllPost: $postEntity" )
//                            postEntityList.add(postEntity)
//                        }
//                }
//            }
//            offer(postEntityList)
//        }
//        awaitClose { subscription.remove() }
//    }
}

