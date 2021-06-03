package com.caunb163.data.repository

import android.net.Uri
import com.caunb163.data.firebase.FireStore
import com.caunb163.domain.model.Post
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await

class RepositoryEditPostImpl {
    private val db = Firebase.firestore
    private val listImage = mutableListOf<String>()
    private var videoPath = ""

    suspend fun editPost(post: Post) {
        if (post.images.isNotEmpty()) {
            for (item in post.images) {
                if (item.contains("firebasestorage")) {
                    listImage.add(item)
                } else {
                    val uriPath = Uri.parse(item)
                    val storageRef =
                        Firebase.storage.reference.child("image/" + uriPath.lastPathSegment)
                    val uploadTask: UploadTask = storageRef.putFile(uriPath)
                    uploadTask.continueWithTask { task ->
                        if (!task.isSuccessful) {
                            task.exception?.let { throw  it }
                        }
                        storageRef.downloadUrl
                    }.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val path = task.result.toString()
                            listImage.add(path)
                        }
                    }.await()
                }
            }
        }

        if (post.video.isNotEmpty()) {
            if (post.video.contains("firebasestorage")) {
                videoPath = post.video
            } else {
                val uriPath = Uri.parse(post.video)
                val storageRef =
                    Firebase.storage.reference.child("video/" + uriPath.lastPathSegment)
                val uploadTask: UploadTask = storageRef.putFile(uriPath)
                uploadTask.continueWithTask { task ->
                    if (!task.isSuccessful) {
                        task.exception?.let { throw  it }
                    }
                    storageRef.downloadUrl
                }.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        videoPath = task.result.toString()
                    }
                }.await()
            }
        }

        val mPost = Post(
            userId = post.userId,
            content = post.content,
            createDate = post.createDate,
            images = listImage,
            arrCmtId = post.arrCmtId.toMutableList(),
            arrLike = post.arrLike.toMutableList(),
            video = videoPath,
            active = post.active
        )

        if (post.isAds) {
            db.collection(FireStore.ADS).document(post.postId).set(mPost).await()
        } else {
            db.collection(FireStore.POST).document(post.postId).set(mPost).await()
        }
    }
}