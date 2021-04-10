package com.caunb163.data.repository

import com.caunb163.data.datalocal.LocalStorage
import com.caunb163.data.firebase.Auth
import com.caunb163.data.firebase.FireStore
import com.caunb163.domain.model.User
import com.caunb163.domain.usecase.login.RepositoryLogin
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class RepositoryLoginImpl(
    private val auth: Auth,
    private val localStorage: LocalStorage
) : RepositoryLogin {
    private val TAG = "RepositoryLoginImpl"
    val db = Firebase.firestore

    override suspend fun loginWithEmailAndPassword(email: String, password: String) {
        val result = auth.authWithEmailAndPassword(email, password)
        val data = db.collection(FireStore.USER).document(result.uid).get().await()
        val user = data.toObject(User::class.java)
        localStorage.saveAccount(user)
    }

    override suspend fun loginWithGoogle(idToken: String) {
        val result = auth.loginWithGoogle(idToken)
        val data = db.collection(FireStore.USER).document(result.uid).get().await()
        val user = data.toObject(User::class.java)
        localStorage.saveAccount(user)
    }

}