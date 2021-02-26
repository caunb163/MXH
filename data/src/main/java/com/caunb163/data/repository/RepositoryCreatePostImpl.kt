package com.caunb163.data.repository

import com.caunb163.data.datalocal.LocalStorage
import com.caunb163.domain.model.Post
import com.caunb163.domain.usecase.home.create_post.RepositoryCreatePost
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class RepositoryCreatePostImpl(
    private val localStorage: LocalStorage
) : RepositoryCreatePost {

    override suspend fun createPost(
        userId: String,
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
            content,
            createDate,
            like,
            likeNumber,
            commentNumber,
            images
        )
        val user = localStorage.getAccount()!!

        db.collection("Posts").add(post).addOnSuccessListener { it ->
            user.arrPostId.add(it.id)
            localStorage.saveAccount(user)
            db.collection("Users").document(userId).update("arrPostId", user.arrPostId)
        }.await()
    }
}