package com.caunb163.data.repository

import com.caunb163.data.mapper.CommentMapper
import com.caunb163.domain.model.Comment
import com.caunb163.domain.model.CommentEntity
import com.caunb163.domain.model.Post
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
    suspend fun getAllComment(postId: String): Flow<List<CommentEntity>> = callbackFlow {
        val db = Firebase.firestore

        val data = db.collection("Posts").document(postId)
        val subscription = data.addSnapshotListener { value, error ->
            if (error != null) {
                return@addSnapshotListener
            }

            val commentEntityList = mutableListOf<CommentEntity>()

            value?.let {
                val post = it.toObject(Post::class.java)
                post?.let { p ->
                    for (cmtId in p.arrCmtId) {
                        db.collection("Comments").document(cmtId).get()
                            .addOnCompleteListener { task ->
                                val cmt = task.result?.toObject(Comment::class.java)
                                cmt?.let { comment ->
                                    db.collection("Users").document(comment.userId).get()
                                        .addOnCompleteListener { t ->
                                            val user = t.result?.toObject(User::class.java)
                                            val cmtEntity = commentMapper.toEntity(
                                                comment,
                                                user?.username ?: "",
                                                user?.photoUrl ?: ""
                                            )

                                            commentEntityList.add(cmtEntity)
                                            commentEntityList.sortByDescending { l ->
                                                l.time.inc()
                                            }
                                            offer(commentEntityList)
                                        }
                                }
                            }
                    }
                }
            }
        }

        awaitClose { subscription.remove() }
    }
}