package com.caunb163.data.repository

import android.net.Uri
import com.caunb163.data.datalocal.LocalStorage
import com.caunb163.data.firebase.FireStore
import com.caunb163.domain.model.Post
import com.caunb163.domain.model.User
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

@ExperimentalCoroutinesApi
class RepositoryProfileImpl {
    private val TAG = "RepositoryProfileImpl"
    private val db = Firebase.firestore

    suspend fun listenerPostChange(userId: String): Flow<Post> = callbackFlow {
        val data =
            db.collection(FireStore.POST).whereEqualTo("userId", userId)
        val subscription = data.addSnapshotListener { value, error ->
            if (error != null) {
                return@addSnapshotListener
            }
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

    suspend fun updateAvatar(uri: String, userId: String) {
        val uriPath = Uri.parse(uri)
        val storageRef = Firebase.storage.reference.child("avatar/" + uriPath.lastPathSegment)
        val uploadTask: UploadTask = storageRef.putFile(uriPath)
        var stringPath = ""
        uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let { throw  it }
            }
            storageRef.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                stringPath = task.result.toString()
                db.collection(FireStore.USER).document(userId).update("photoUrl", stringPath)
            }
        }.await()
//        return stringPath
    }

    suspend fun updateBackground(uri: String, userId: String) {
        val uriPath = Uri.parse(uri)
        val storageRef = Firebase.storage.reference.child("background/" + uriPath.lastPathSegment)
        val uploadTask: UploadTask = storageRef.putFile(uriPath)
        var stringPath = ""
        uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let { throw  it }
            }
            storageRef.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                stringPath = task.result.toString()
                db.collection(FireStore.USER).document(userId)
                    .update("photoBackground", stringPath)
            }
        }.await()
//        return stringPath
    }

    suspend fun updateProfile(user: User) {
        db.collection(FireStore.USER).document(user.userId).set(user).await()
    }
}