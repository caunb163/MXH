package com.caunb163.data.repository

import com.caunb163.data.firebase.Auth
import com.caunb163.data.firebase.FB
import com.caunb163.domain.model.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

@ExperimentalCoroutinesApi
class RepositoryMainImpl(private val auth: Auth) {
    private val TAG = "RepositoryMainImpl"
    private val db = Firebase.firestore

    suspend fun saveUser(): Flow<User> = callbackFlow {
        val userId = auth.getUser().uid
        val data = db.collection(FB.USER).document(userId)

        val subscription = data.addSnapshotListener { value, error ->
            if (error != null) {
                return@addSnapshotListener
            }
            var user = User()
            value?.let {
                user = value.toObject(User::class.java)!!
                offer(user)
            }
        }
        awaitClose { subscription.remove() }
    }
}