package com.caunb163.data.repository

import android.net.Uri
import android.util.Log
import com.caunb163.data.datalocal.LocalStorage
import com.caunb163.data.firebase.FireStore
import com.caunb163.domain.model.Post
import com.caunb163.domain.usecase.home.create_post.RepositoryCreatePost
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await

class RepositoryCreatePostImpl(
    private val localStorage: LocalStorage
) : RepositoryCreatePost {
    private val TAG = "RepositoryCreatePostImp"

    override suspend fun createPost(
        userId: String,
        createDate: Long,
        images: List<String>,
        content: String,
        arrCmtId: MutableList<String>,
        arrLike: MutableList<String>,
        video: String,
        isAds: Boolean
    ) {
        val db = Firebase.firestore
        val listImage = mutableListOf<String>()
        val user = localStorage.getAccount()!!

        if (images.isNotEmpty()) {
            for (item in images) {
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

        var videoPath = ""
        if (video.isNotEmpty()){
            val uriPath = Uri.parse(video)
            val storageRef = Firebase.storage.reference.child("video/" + uriPath.lastPathSegment)
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

        val post = Post(
            userId = userId,
            content = content,
            createDate = createDate,
            images = listImage,
            arrCmtId = arrCmtId,
            arrLike = arrLike,
            video = videoPath,
            active = true
        )
        post.isAds = isAds

        if (isAds) {
            db.collection(FireStore.ADS).add(post).addOnSuccessListener {
                user.arrPostId.add(it.id)
                db.collection(FireStore.USER).document(userId).update("arrPostId", user.arrPostId)
            }.await()
        } else {
            db.collection(FireStore.POST).add(post).addOnSuccessListener {
                user.arrPostId.add(it.id)
                db.collection(FireStore.USER).document(userId).update("arrPostId", user.arrPostId)
            }.await()
        }

    }
}