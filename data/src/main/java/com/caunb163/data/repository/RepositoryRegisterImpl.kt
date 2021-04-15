package com.caunb163.data.repository

import com.caunb163.data.datalocal.LocalStorage
import com.caunb163.data.firebase.Auth
import com.caunb163.data.firebase.FireStore
import com.caunb163.domain.model.User
import com.caunb163.domain.usecase.register.RepositoryRegister
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class RepositoryRegisterImpl(
    private val auth: Auth,
    private val localStorage: LocalStorage
) : RepositoryRegister {
    val db = Firebase.firestore

    override suspend fun register(
        username: String,
        email: String,
        password: String
    ) {
        val result = auth.createAccount(username, email, password)
        val data = db.collection(FireStore.USER).document(result.uid).get().await()
        val user = data.toObject(User::class.java)
        localStorage.saveAccount(user)
    }
}