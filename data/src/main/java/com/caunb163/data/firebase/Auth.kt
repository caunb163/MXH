package com.caunb163.data.firebase

import com.caunb163.domain.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class Auth {
    private val auth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    private val db = Firebase.firestore

    suspend fun authWithEmailAndPassword(
        email: String, password: String
    ): FirebaseUser {
        auth.signInWithEmailAndPassword(email, password).await()
        return auth.currentUser ?: throw FirebaseAuthException("firebase Auth exception", "")
    }

    suspend fun createAccount(
        username: String, email: String, password: String, phone: String
    ): FirebaseUser {
        auth.createUserWithEmailAndPassword(email, password).await()
        val result = auth.currentUser
        result?.let {
            val user = User(
                username = username,
                email = it.email ?: "",
                photoUrl = it.photoUrl?.toString() ?: "",
                arrPostId = mutableListOf(),
                userId = it.uid,
                phone = phone,
                photoBackground = "",
                birthDay = "",
                address = "",
                intro = "",
            )

            db.collection("Users").document(it.uid).set(user)
        }
        return auth.currentUser ?: throw FirebaseAuthException("firebase Auth exception", "")
    }

}