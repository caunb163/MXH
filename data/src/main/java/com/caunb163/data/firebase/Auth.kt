package com.caunb163.data.firebase

import com.caunb163.domain.model.User
import com.google.firebase.auth.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class Auth {
    private val auth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    private val db = Firebase.firestore

    fun getUser(): FirebaseUser {
        return auth.currentUser ?: throw FirebaseAuthException("firebase Auth exception", "")
    }

    suspend fun authWithEmailAndPassword(
        email: String, password: String
    ): FirebaseUser {
        auth.signInWithEmailAndPassword(email, password).await()
        return auth.currentUser ?: throw FirebaseAuthException("firebase Auth exception", "")
    }

    suspend fun createAccount(
        username: String, email: String, password: String
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
                phone = it.phoneNumber ?: "",
                photoBackground = "",
                birthDay = "",
                gender = "",
                intro = "",
            )

            db.collection(FireStore.USER).document(it.uid).set(user)
        }
        return auth.currentUser ?: throw FirebaseAuthException("firebase Auth exception", "")
    }

    suspend fun loginWithGoogle(idToken: String): FirebaseUser {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential).await()
        val result = auth.currentUser
        result?.let {
            val data = db.collection(FireStore.USER).document(it.uid).get().await()
            var user = data.toObject(User::class.java)
            if (user == null) {
                user = User(
                    username = it.displayName ?: "",
                    email = it.email ?: "",
                    photoUrl = it.photoUrl?.toString() ?: "",
                    arrPostId = mutableListOf(),
                    userId = it.uid,
                    phone = it.phoneNumber ?: "",
                    photoBackground = "",
                    birthDay = "",
                    gender = "",
                    intro = ""
                )
                db.collection(FireStore.USER).document(it.uid).set(user).await()
            }
        }
        return auth.currentUser ?: throw Exception("Firebase Auth exception")
    }

    suspend fun getUserPhone(): User? {
        val result = auth.currentUser
        val data = db.collection(FireStore.USER).document(result!!.uid).get().await()
        return data.toObject(User::class.java)
    }

    suspend fun checkPhone(verificationId: String, opt: String): Boolean {
        val credential = PhoneAuthProvider.getCredential(verificationId, opt)
        auth.signInWithCredential(credential).await()
        val result = auth.currentUser
        result?.let {
            val data = db.collection(FireStore.USER).document(it.uid).get().await()
            val user = data.toObject(User::class.java)
            if (user != null) {
                return true
            }
        }
        return false
    }

    suspend fun createUser(username: String, birthday: String, gender: String): FirebaseUser {
        val result = auth.currentUser
        result?.let {
            val data = db.collection(FireStore.USER).document(it.uid).get().await()
            var user = data.toObject(User::class.java)
            if (user == null) {
                user = User(
                    username = username,
                    email = it.email ?: "",
                    photoUrl = it.photoUrl?.toString() ?: "",
                    arrPostId = mutableListOf(),
                    userId = it.uid,
                    phone = it.phoneNumber ?: "",
                    photoBackground = "",
                    birthDay = birthday,
                    gender = gender,
                    intro = ""
                )
                db.collection(FireStore.USER).document(it.uid).set(user).await()
            }
        }
        return auth.currentUser ?: throw Exception("Firebase Auth exception")
    }
}