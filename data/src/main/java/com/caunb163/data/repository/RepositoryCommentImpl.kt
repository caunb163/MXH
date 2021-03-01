package com.caunb163.data.repository

import android.util.Log
import com.caunb163.data.mapper.CommentMapper
import com.caunb163.domain.model.Comment
import com.caunb163.domain.model.CommentEntity
import com.caunb163.domain.model.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

@ExperimentalCoroutinesApi
class RepositoryCommentImpl(
    private val commentMapper: CommentMapper
) {
    private val TAG = "RepositoryCommentImpl"
    suspend fun getAllComment(): Flow<List<CommentEntity>> = callbackFlow {
        val db = Firebase.firestore
        val data = db.collection("Comments")

        val subscription = data.addSnapshotListener { value, error ->
            if (error != null) {
                return@addSnapshotListener
            }

            val commentEntityList = mutableListOf<CommentEntity>()

            value?.let {
                for (result in value) {
                    val comment = result.toObject(Comment::class.java)
                    db.collection("Users").document(comment.userId).get()
                        .addOnCompleteListener { task ->
                            val user = task.result?.toObject(User::class.java)
                            val commentEntity = commentMapper.toEntity(
                                comment,
                                user?.username ?: "",
                                user?.photoUrl ?: ""
                            )
                            commentEntityList.add(commentEntity)
                            commentEntityList.sortByDescending { it.time.inc() }
                            offer(commentEntityList)
                        }
                }
            }
        }
        awaitClose { subscription.remove() }
    }
}