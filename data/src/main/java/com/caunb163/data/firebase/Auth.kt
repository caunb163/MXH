package com.caunb163.data.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await

class Auth {
    private val auth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

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

}