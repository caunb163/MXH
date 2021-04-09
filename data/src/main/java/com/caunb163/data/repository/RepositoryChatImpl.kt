package com.caunb163.data.repository

import android.net.Uri
import android.util.Log
import com.caunb163.data.firebase.FireStore
import com.caunb163.data.mapper.MessageMapper
import com.caunb163.domain.model.*
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
import java.lang.Double.parseDouble

@ExperimentalCoroutinesApi
class RepositoryChatImpl(
    private val messageMapper: MessageMapper
) {
    private val TAG = "RepositoryChatImpl"
    private val db = Firebase.firestore

    suspend fun listenerMessageChange(groupId: String): Flow<MessageEntity> = callbackFlow {
        val data =
            db.collection(FireStore.MESSAGE).whereEqualTo("groupId", groupId)
        val subscription = data.addSnapshotListener { value, error ->
            if (error != null) {
                return@addSnapshotListener
            }
            value?.let { qs ->
                for (dc in qs.documentChanges) {
                    val message = dc.document.toObject(Message::class.java)
                    db.collection(FireStore.USER).document(message.userId).get()
                        .addOnCompleteListener { task ->
                            val user = task.result?.toObject(User::class.java)
                            user?.let { u ->
                                val messageEntity = messageMapper.toEntity(
                                    message,
                                    u.username,
                                    u.photoUrl,
                                    dc.document.id
                                )
                                when (dc.type) {
                                    DocumentChange.Type.ADDED -> {
//                                        Log.e(TAG, "listenerMessageChange: ADD ${dc.document.data}")
                                        offer(messageEntity)
                                    }
                                    DocumentChange.Type.MODIFIED -> {
//                                        Log.e(TAG, "listenerMessageChange: MODIFIED ${dc.document.data}")
                                        offer(messageEntity)
                                    }
                                    DocumentChange.Type.REMOVED -> {
//                                        Log.e(TAG, "listenerMessageChange: REMOVED ${dc.document.data}")
                                        offer(MessageEntity(messageId = messageEntity.messageId))
                                    }
                                }
                            }
                        }
                }
            }
        }
        awaitClose { subscription.remove() }
    }

    suspend fun createMessenger(
        content: String,
        createDate: Long,
        groupId: String,
        userId: String,
        image: String
    ) {

        var imgUri = ""
        var boolean = true
        if (image.isNotEmpty()) {

            try {
                parseDouble(image)
            } catch (e: NumberFormatException) {
                boolean = false
            }
            if (!boolean) {
                val uriPath = Uri.parse(image)
                val storageRef = Firebase.storage.reference.child("chat/" + uriPath.lastPathSegment)
                val uploadTask: UploadTask = storageRef.putFile(uriPath)
                uploadTask.continueWithTask { task ->
                    if (!task.isSuccessful) task.exception?.let { throw  it }
                    storageRef.downloadUrl
                }.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        imgUri = task.result.toString()
                    }
                }.await()
            } else {
                imgUri = image
            }
        }

        val message = Message(
            userId, groupId, content, createDate, imgUri
        )
        db.collection(FireStore.MESSAGE).add(message).await()
        if (content.isNotEmpty()) {
            db.collection(FireStore.GROUP).document(groupId)
                .update("lastMessage", content, "createDate", createDate).await()
        } else {
            if (image.isNotEmpty()) {
                db.collection(FireStore.GROUP).document(groupId)
                    .update("lastMessage", "Đã gửi một nhãn dán", "createDate", createDate).await()
            }
        }
    }
}