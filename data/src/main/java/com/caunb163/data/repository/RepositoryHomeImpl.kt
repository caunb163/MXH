package com.caunb163.data.repository

import com.caunb163.domain.model.Post
import com.caunb163.domain.usecase.home.RepositoryHome
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class RepositoryHomeImpl : RepositoryHome {
    private val TAG = "RepositoryHomeImpl"

    @ExperimentalCoroutinesApi
    override suspend fun getAllPost(): Flow<List<Post>> = callbackFlow {
        val db = Firebase.firestore
        val postsList = mutableListOf<Post>()

        val data =
            db.collection("Posts").orderBy("createDate", Query.Direction.DESCENDING)

        val subscription = data.addSnapshotListener { value, error ->
            if (error != null) {
                return@addSnapshotListener
            }
            postsList.clear()
            postsList.addAll(
                value!!.toList().toMutableList().map { it.toObject(Post::class.java) })
            offer(postsList)
        }

        awaitClose { subscription.remove() }
    }
}

