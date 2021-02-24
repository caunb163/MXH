package com.caunb163.data.repository

import com.caunb163.domain.model.Post
import com.caunb163.domain.model.User
import com.caunb163.domain.usecase.home.create_post.RepositoryCreatePost
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class RepositoryCreatePostImpl : RepositoryCreatePost {

    override suspend fun createPost(
        userId: String,
        userName: String,
        userAvatar: String,
        likeNumber: Int,
        like: Boolean,
        createDate: Long,
        images: List<String>,
        content: String,
        commentNumber: Int
    ) {
        val db = Firebase.firestore
        val post = Post(
            userId,
            userName,
            userAvatar,
            content,
            createDate,
            like,
            likeNumber,
            commentNumber,
            images
        )
        val data =
            db.collection("Users").document(userId).get().await()
        val user = data.toObject(User::class.java)!!

        db.collection("Posts").add(post).addOnSuccessListener { it ->
            user.arrPostId.add(it.id)
            db.collection("Users").document(userId).update("arrPostId", user.arrPostId);
        }.await()
    }
}