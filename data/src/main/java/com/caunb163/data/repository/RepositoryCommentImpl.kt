package com.caunb163.data.repository

import android.net.Uri
import com.caunb163.data.firebase.FireStore
import com.caunb163.domain.model.Comment
import com.caunb163.domain.model.Post
import com.google.firebase.firestore.DocumentChange
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
class RepositoryCommentImpl {
    val db = Firebase.firestore

    suspend fun listenerComment(postId: String): Flow<Comment> = callbackFlow {
        val data =
            db.collection(FireStore.COMMENT).whereEqualTo("postId", postId)
        val subscription = data.addSnapshotListener { value, error ->
            if (error != null) {
                return@addSnapshotListener
            }
            value?.let { qs ->
                val list =
                    qs.documentChanges.sortedWith(compareBy { it.document.toObject(Comment::class.java).time.inc() })
                for (dc in list) {
                    val comment = dc.document.toObject(Comment::class.java)
                    comment.commentId = dc.document.id
                    when (dc.type) {
                        DocumentChange.Type.ADDED -> {
                            offer(comment)
                        }
                        DocumentChange.Type.MODIFIED -> {
                            offer(comment)
                        }
                        DocumentChange.Type.REMOVED -> {
                            val cmt = Comment()
                            cmt.commentId = dc.document.id
                            offer(cmt)
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