package com.caunb163.data.repository

import android.util.Log
import com.caunb163.domain.model.Post
import com.caunb163.domain.usecase.home.RepositoryHome
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class RepositoryHomeImpl : RepositoryHome {
    private val TAG = "RepositoryHomeImpl"

    override suspend fun getAllPost(): List<Post> {
        val db = Firebase.firestore
        val postsList = mutableListOf<Post>()
//        db.collection("Posts").get().addOnCompleteListener { task ->
//            if (task.isSuccessful) {
//                for (document in task.result!!) {
//
//                    val name = document.data
//                    Log.e(TAG, "getAllPost: $name")
//                }
//            }
//        }.await()
        val data = db.collection("Posts").get().await()
        var list = mutableListOf<DocumentSnapshot>()
        list = data.toList().toMutableList()
        postsList.addAll(list.map { it.toObject(Post::class.java)!!})
        return postsList
    }
}

