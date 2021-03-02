package com.caunb163.data.repository

import android.net.Uri
import com.caunb163.data.datalocal.LocalStorage
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
    override suspend fun createPost(
        userId: String,
        createDate: Long,
        images: List<String>,
        content: String,
        arrCmtId: MutableList<String>,
        arrLike: MutableList<String>
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

        val post = Post(
            userId = userId,
            content = content,
            createDate = createDate,
            images = listImage,
            arrCmtId = arrCmtId,
            arrLike = arrLike
        )

        db.collection("Posts").add(post).addOnSuccessListener { it ->
            user.arrPostId.add(it.id)
            localStorage.saveAccount(user)
            db.collection("Users").document(userId).update("arrPostId", user.arrPostId)
            db.collection("Posts").document(it.id).update("postId",it.id)
        }.await()
    }
}