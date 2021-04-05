package com.caunb163.data.repository

import com.caunb163.data.firebase.FB
import com.caunb163.data.mapper.MessageMapper
import com.caunb163.domain.model.*
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

@ExperimentalCoroutinesApi
class RepositoryChatImpl(
    private val messageMapper: MessageMapper
) {
    private val TAG = "RepositoryChatImpl"
    private val db = Firebase.firestore

//    suspend fun getAllMessages(groupId: String): MutableList<MessageEntity> {
//        val data = db.collection(FB.MESSAGE).whereEqualTo("groupId", groupId).get().await()
//        val messageEntityList = mutableListOf<MessageEntity>()
//
//        for (result in data.documents) {
//            val message = result.toObject(Message::class.java)
//            message?.let {
//                db.collection(FB.USER).document(it.userId).get().addOnCompleteListener { task ->
//                    val user = task.result?.toObject(User::class.java)
//                    user?.let { u ->
//                        val messageEntity = messageMapper.toEntity(
//                            it, u.username, u.photoUrl, result.id
//                        )
//                        messageEntityList.add(messageEntity)
//                    }
//                }.await()
//            }
//        }
//
//        messageEntityList.sortBy { it.createDate.dec() }
//        return messageEntityList
//    }

    suspend fun listenerMessageChange(groupId: String): Flow<MessageEntity> = callbackFlow {
        val data =
            db.collection(FB.MESSAGE).whereEqualTo("groupId", groupId)
        val subscription = data.addSnapshotListener { value, error ->
            if (error != null) {
                return@addSnapshotListener
            }
            value?.let { qs ->
                for (dc in qs.documentChanges) {
                    val message = dc.document.toObject(Message::class.java)
                    db.collection(FB.USER).document(message.userId).get()
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
//                                        Log.e(TAG, "listenerPostChange: ADD ${dc.document.data}")
                                        offer(messageEntity)
                                    }
                                    DocumentChange.Type.MODIFIED -> {
//                                        Log.e(TAG, "listenerPostChange: MODIFIED ${dc.document.data}")
                                        offer(messageEntity)
                                    }
                                    DocumentChange.Type.REMOVED -> {
//                                        Log.e(TAG, "listenerPostChange: REMOVED ${PostEntity(postId = postEntity.postId)}")
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
        val message = Message(
            userId, groupId, content, createDate, image
        )
        db.collection(FB.MESSAGE).add(message).await()
        db.collection(FB.GROUP).document(groupId).update("lastMessage", content).await()
    }
}