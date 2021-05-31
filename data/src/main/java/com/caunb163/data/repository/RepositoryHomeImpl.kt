package com.caunb163.data.repository

import android.util.Log
import com.caunb163.data.datalocal.LocalStorage
import com.caunb163.data.firebase.FireStore
import com.caunb163.data.mapper.PostMapper
import com.caunb163.domain.model.Post
import com.caunb163.domain.model.PostEntity
import com.caunb163.domain.model.User
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.Query
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

    suspend fun listenerPostChange(): Flow<Post> = callbackFlow {
        val data = db.collection(FireStore.POST).orderBy("createDate", Query.Direction.DESCENDING)
        val subscription = data.addSnapshotListener { value, error ->
            if (error != null) return@addSnapshotListener
            value?.let { qs ->
                for (dc in qs.documentChanges) {
                    val post = dc.document.toObject(Post::class.java)
                    post.postId = dc.document.id
                    when (dc.type) {
                        DocumentChange.Type.ADDED -> {
//                            Log.e(TAG, "listenerPostChange: ADD ${dc.document.data}")
                            offer(post)
                        }
                        DocumentChange.Type.MODIFIED -> {
//                            Log.e(TAG, "listenerPostChange: MODIFIED ${dc.document.data}")
                            offer(post)
                        }
                        DocumentChange.Type.REMOVED -> {
//                            Log.e(TAG, "listenerPostChange: REMOVED}")
                            post.remove = true
                            offer(post)
                        }
                    }
                }
            }
        }
        awaitClose { subscription.remove() }
    }

    suspend fun getPostEntity(post: Post): PostEntity {
        var postEntity = PostEntity()
        postEntity.postId = post.postId
        if (!post.remove) {
            db.collection(FireStore.USER).document(post.userId).get()
                .addOnCompleteListener { task ->
                    val user = task.result?.toObject(User::class.java)
                    user?.let { u ->
                        postEntity = postMapper.toEntity(post, post.postId, u.username, u.photoUrl)
                    }
                }.await()
        }
        return postEntity
    }

    suspend fun likePost(postId: String) {
        db.collection(FireStore.POST).document(postId).get().addOnCompleteListener { task ->
            val post = task.result?.toObject(Post::class.java)
            post?.let { p ->
                val arrLike = p.arrLike
                if (arrLike.contains(user.userId)) {
                    arrLike.remove(user.userId)
                } else {
                    arrLike.add(user.userId)
                }
                db.collection(FireStore.POST).document(postId).update("arrLike", arrLike)
            }
        }.await()
    }

    suspend fun deletePost(post: PostEntity) {
        for (item in post.arrCmtId) {
            db.collection(FireStore.COMMENT).document(item).delete().await()
        }
        db.collection(FireStore.POST).document(post.postId).delete().await()
        db.collection(FireStore.USER).document(post.userId).get().addOnCompleteListener { task ->
            val user = task.result?.toObject(User::class.java)
            user?.let { u ->
                u.arrPostId.remove(post.postId)
                db.collection(FireStore.USER).document(post.userId).update("arrPostId", u.arrPostId)
            }
        }.await()
    }

    fun logout() {
        Firebase.auth.signOut()
        localStorage.saveAccount(null)
    }

    suspend fun getAds(): MutableList<Post> {
        val list = mutableListOf<Post>()
        db.collection(FireStore.ADS).get().addOnSuccessListener { task ->
            for (item in task.documents) {
                val post = item.toObject(Post::class.java)
                post?.let {
                    post.postId = item.id
                    post.isAds = true
                    list.add(post)
                }
            }
        }.await()
        return list
    }

    suspend fun getAdsEntity(list: MutableList<Post>): MutableList<PostEntity> {
        val listEntity = mutableListOf<PostEntity>()
        for (item in list) {
            db.collection(FireStore.USER).document(item.userId).get()
                .addOnCompleteListener { task ->
                    val user = task.result?.toObject(User::class.java)
                    user?.let { u ->
                        val postEntity =
                            postMapper.toEntity(item, item.postId, u.username, u.photoUrl)
                        postEntity.isAds = item.isAds
                        listEntity.add(postEntity)
                    }
                }.await()
        }
        return listEntity
    }
}

