package com.caunb163.data.repository

import android.net.Uri
import com.caunb163.data.firebase.FireStore
import com.caunb163.data.mapper.CommentMapper
import com.caunb163.domain.model.Comment
import com.caunb163.domain.model.CommentEntity
import com.caunb163.domain.model.Post
import com.caunb163.domain.model.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

@ExperimentalCoroutinesApi
class RepositoryCommentImpl(
    private val commentMapper: CommentMapper
) {
    private val TAG = "RepositoryCommentImpl"
    val db = Firebase.firestore

    suspend fun getAllComment(postId: String): Flow<List<CommentEntity>> = callbackFlow {
        val data = db.collection(FireStore.POST).document(postId)
        val subscription = data.addSnapshotListener { value, error ->
            if (error != null) {
                return@addSnapshotListener
            }

            val commentEntityList = mutableListOf<CommentEntity>()

            value?.let {
                val post = it.toObject(Post::class.java)
                post?.let { p ->
                    for (cmtId in p.arrCmtId) {
                        db.collection(FireStore.COMMENT).document(cmtId).get()
                            .addOnCompleteListener { task ->
                                val cmt = task.result?.toObject(Comment::class.java)
                                cmt?.let { comment ->
                                    db.collection(FireStore.USER).document(comment.userId).get()
                                        .addOnCompleteListener { t ->
                                            val user = t.result?.toObject(User::class.java)
                                            val cmtEntity = commentMapper.toEntity(
                                                comment,
                                                user?.username ?: "",
                                                user?.photoUrl ?: ""
                                            )

                                            commentEntityList.add(cmtEntity)
                                            commentEntityList.sortBy { it.time.inc() }
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



    suspend fun createComment(
        userId: String,
        time: Long,
        postId: String,
        image: String,
        content: String
    ) {
        var imgUri = ""
        if (image.isNotEmpty()) {
            val uriPath = Uri.parse(image)
            val storageRef = Firebase.storage.reference.child("comment/" + uriPath.lastPathSegment)
            val uploadTask: UploadTask = storageRef.putFile(uriPath)
            uploadTask.continueWithTask { task ->
                if (!task.isSuccessful) task.exception?.let { throw  it }
                storageRef.downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    imgUri = task.result.toString()
                }
            }.await()
        }

        val comment = Comment(
            postId = postId,
            content = content,
            userId = userId,
            image = imgUri,
            time = time
        )

        db.collection(FireStore.COMMENT).add(comment).addOnSuccessListener { it ->
            db.collection(FireStore.POST).document(postId).get().addOnCompleteListener { task ->
                val post = task.result?.toObject(Post::class.java)
                post?.let { p ->
                    val arrCmtId = p.arrCmtId
                    arrCmtId.add(it.id)
                    db.collection(FireStore.POST).document(postId).update("arrCmtId", arrCmtId)
                }
            }
        }.await()
    }
}