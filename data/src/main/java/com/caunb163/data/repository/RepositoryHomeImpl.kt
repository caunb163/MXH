package com.caunb163.data.repository

import com.caunb163.data.datalocal.LocalStorage
import com.caunb163.data.firebase.FireStore
import com.caunb163.domain.model.Post
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
    private val localStorage: LocalStorage
) {
    private val TAG = "RepositoryHomeImpl"
    private val db = Firebase.firestore
    private val user = localStorage.getAccount()!!

    suspend fun listenerPost(): Flow<Post> = callbackFlow {
        val data = db.collection(FireStore.POST).orderBy("createDate", Query.Direction.DESCENDING)
        val subscription = data.addSnapshotListener { value, error ->
            if (error != null) return@addSnapshotListener
            value?.let { qs ->
                for (dc in qs.documentChanges) {
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

    suspend fun deletePost(post: Post) {
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

    suspend fun listenerAdsPost(): Flow<Post> = callbackFlow {
        val data = db.collection(FireStore.ADS)
        val subscription = data.addSnapshotListener { value, error ->
            if (error != null) return@addSnapshotListener
            value?.let { qs ->
                for (dc in qs.documentChanges) {
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

