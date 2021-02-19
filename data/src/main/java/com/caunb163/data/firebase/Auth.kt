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
//        var user = auth.currentUser
//        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
//            user = if (task.isSuccessful) auth.currentUser!!
//             else null
//
//        }
//        return user ?: throw FirebaseAuthException("firebase Auth exception", "")
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
                it.uid,
                username,
                it.email ?: "",
                it.photoUrl?.toString() ?: ""
            )

            db.collection("Users").document(it.uid).set(user)
        }
        return auth.currentUser ?: throw FirebaseAuthException("firebase Auth exception", "")
    }
}