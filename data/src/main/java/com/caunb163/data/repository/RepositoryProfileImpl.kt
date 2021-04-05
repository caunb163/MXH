package com.caunb163.data.repository

import android.net.Uri
import com.caunb163.data.datalocal.LocalStorage
import com.caunb163.data.firebase.FB
import com.caunb163.data.mapper.PostMapper
import com.caunb163.domain.model.Post
import com.caunb163.domain.model.PostEntity
import com.caunb163.domain.model.User
import com.google.firebase.firestore.DocumentChange
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
class RepositoryProfileImpl(
    private val localStorage: LocalStorage,
    private val postMapper: PostMapper
) {
    private val TAG = "RepositoryProfileImpl"
    private val db = Firebase.firestore
    val user = localStorage.getAccount()!!

    suspend fun listenerPostChange(): Flow<PostEntity> = callbackFlow {
        val data =
            db.collection(FB.POST).whereEqualTo("userId", user.userId)
        val subscription = data.addSnapshotListener { value, error ->
            if (error != null) {
                return@addSnapshotListener
            }
            value?.let { qs ->
                for (dc in qs.documentChanges) {
                    val post = dc.document.toObject(Post::class.java)
                    db.collection(FB.USER).document(post.userId).get()
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
//                                        Log.e(TAG, "listenerPostChange: ADD ${dc.document.data}")
                                        offer(postEntity)
                                    }
                                    DocumentChange.Type.MODIFIED -> {
//                                        Log.e(TAG, "listenerPostChange: MODIFIED ${dc.document.data}")
                                        offer(postEntity)
                                    }
                                    DocumentChange.Type.REMOVED -> {
//                                        Log.e(TAG, "listenerPostChange: REMOVED ${PostEntity(postId = postEntity.postId)}")
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

    suspend fun updateAvatar(uri: String): String {
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
                db.collection(FB.USER).document(user.userId).update("photoUrl", stringPath)
            }
        }.await()

        return stringPath
    }

    suspend fun updateBackground(uri: String): String {
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
                db.collection(FB.USER).document(user.userId).update("photoBackground", stringPath)
            }
        }.await()

        return stringPath
    }
}