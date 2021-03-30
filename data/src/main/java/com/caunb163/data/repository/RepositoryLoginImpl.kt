package com.caunb163.data.repository

import com.caunb163.data.datalocal.LocalStorage
import com.caunb163.data.firebase.Auth
import com.caunb163.data.firebase.FB
import com.caunb163.domain.model.User
import com.caunb163.domain.usecase.login.RepositoryLogin
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class RepositoryLoginImpl(
    private val auth: Auth,
    private val localStorage: LocalStorage
) : RepositoryLogin {
    override suspend fun loginWithEmailAndPassword(email: String, password: String) {
        val result = auth.authWithEmailAndPassword(email, password)
        val db = Firebase.firestore

        val data = db.collection(FB.USER).document(result.uid).get().await()
        val user = data.toObject(User::class.java)
        localStorage.saveAccount(user)
    }
}