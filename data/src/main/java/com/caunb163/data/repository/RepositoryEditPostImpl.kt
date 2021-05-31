package com.caunb163.data.repository

import android.net.Uri
import com.caunb163.data.firebase.FireStore
import com.caunb163.domain.model.Post
import com.caunb163.domain.model.PostEntity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await

class RepositoryEditPostImpl {
    private val db = Firebase.firestore
    private val listImage = mutableListOf<String>()
    private var videoPath = ""

    suspend fun editPost(postEntity: PostEntity) {
        if (postEntity.images.isNotEmpty()) {
            for (item in postEntity.images) {
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

        if (postEntity.video.isNotEmpty()) {
            if (postEntity.video.contains("firebasestorage")) {
                videoPath = postEntity.video
            } else {
                val uriPath = Uri.parse(postEntity.video)
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

        val post = Post(
            userId = postEntity.userId,
            content = postEntity.content,
            createDate = postEntity.createDate,
            images = listImage,
            arrCmtId = postEntity.arrCmtId.toMutableList(),
            arrLike = postEntity.arrLike.toMutableList(),
            video = videoPath,
            active = postEntity.active
        )

        if (postEntity.isAds) {
            db.collection(FireStore.ADS).document(postEntity.postId).set(post).await()
        } else {
            db.collection(FireStore.POST).document(postEntity.postId).set(post).await()
        }
    }
}