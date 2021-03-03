package com.caunb163.data.repository

import android.net.Uri
import android.util.Log
import com.caunb163.data.datalocal.LocalStorage
import com.caunb163.data.mapper.PostMapper
import com.caunb163.domain.model.Post
import com.caunb163.domain.model.PostEntity
import com.caunb163.domain.model.User
import com.caunb163.domain.usecase.profile.RepositoryProfile
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await

class RepositoryProfileImpl(
    private val localStorage: LocalStorage,
    private val postMapper: PostMapper
) : RepositoryProfile {
    private val TAG = "RepositoryProfileImpl"
    private val db = Firebase.firestore
    val user = localStorage.getAccount()!!

    override suspend fun getProfilePost(): List<PostEntity> {
        val list = mutableListOf<PostEntity>()
        for (postId in user.arrPostId) {
            val data = db.collection("Posts").document(postId).get().await()
            val post = data.toObject(Post::class.java)
            db.collection("Users").document(post!!.userId).get().addOnCompleteListener { task ->
                val user = task.result?.toObject(User::class.java)
                user?.let { u ->
                    val postEntity =
                        postMapper.toEntity(post, postId, u.username, u.photoUrl)
                    list.add(postEntity)
                }

            }.await()
        }
        list.sortByDescending { it.createDate.inc() }
        return list
    }

    override suspend fun updateAvatar(uri: String): String {
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
                db.collection("Users").document(user.userId).update("photoUrl", stringPath)
            }
        }.await()

        return stringPath
    }

    override suspend fun updateBackground(uri: String): String {
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
                db.collection("Users").document(user.userId).update("photoBackground", stringPath)
            }
        }.await()

        return stringPath
    }
}