package com.caunb163.data.repository

import com.caunb163.data.firebase.FireStore
import com.caunb163.domain.model.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class RepositoryUser {
    private val db = Firebase.firestore

    suspend fun getUser(userId: String): User {
        var user = User()
        db.collection(FireStore.USER).document(userId).get().addOnCompleteListener { task ->
            task.result?.toObject(User::class.java)?.let { u ->
                user = u
            }
        }.await()
        return user
    }
}