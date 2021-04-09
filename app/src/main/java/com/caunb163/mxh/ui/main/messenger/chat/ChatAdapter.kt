package com.caunb163.mxh.ui.main.messenger.chat

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.ViewFlipper
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.caunb163.domain.model.MessageEntity
import com.caunb163.domain.model.User
import com.caunb163.mxh.R
import de.hdodenhof.circleimageview.CircleImageView
import java.lang.Double
import java.lang.Double.parseDouble

class ChatAdapter(
    private val glide: RequestManager,
    private val user: User
) : RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    private var list: MutableList<MessageEntity> = mutableListOf()

    fun updateData(datas: MutableList<MessageEntity>) {
        list = datas
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        return ChatViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.layout_chat, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        holder.bind(list[position], user, glide)
    }

    override fun getItemCount(): Int = list.size

    override fun onViewRecycled(holder: ChatViewHolder) {
        super.onViewRecycled(holder)
        holder.unbind()
    }

    class ChatViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val viewFlipper: ViewFlipper = view.findViewById(R.id.chat_viewflipper)
        private val avatar1: CircleImageView = view.findViewById(R.id.chat_avatar1)
        private val message1: TextView = view.findViewById(R.id.chat_message1)
        private val message2: TextView = view.findViewById(R.id.chat_message2)
        private val imageview1: ImageView = view.findViewById(R.id.chat_image1)
        private val imageview2: ImageView = view.findViewById(R.id.chat_image2)

        fun bind(messageEntity: MessageEntity, user: User, glide: RequestManager) {
            var boolean = true
            var imageInt = 0
            try {
                if (messageEntity.image.isNotEmpty()) {
                    imageInt = parseDouble(messageEntity.image).toInt()
                }
            } catch (e: NumberFormatException) {
                boolean = false
            }

            if (TextUtils.equals(messageEntity.userId, user.userId)) {
                viewFlipper.displayedChild = 1
                if (messageEntity.content.isEmpty()) {
                    message2.visibility = View.GONE
                } else {
                    message2.visibility = View.VISIBLE
                    message2.text = messageEntity.content
                }

                if (messageEntity.image.isEmpty()) {
                    imageview2.visibility = View.GONE
                } else {
                    imageview2.visibility = View.VISIBLE
                    imageview2.clipToOutline = true
                    if (boolean) {
                        glide.applyDefaultRequestOptions(RequestOptions())
                            .load(imageInt)
                            .into(imageview2)
                    } else
                        glide.applyDefaultRequestOptions(RequestOptions()).load(messageEntity.image)
                            .into(imageview2)
                }
            } else {
                viewFlipper.displayedChild = 0
                glide.applyDefaultRequestOptions(RequestOptions()).load(messageEntity.userAvatar)
                    .into(avatar1)
                if (messageEntity.content.isEmpty()) {
                    message1.visibility = View.GONE
                } else {
                    message1.visibility = View.VISIBLE
                    message1.text = messageEntity.content
                }

                if (messageEntity.image.isEmpty()) {
                    imageview1.visibility = View.GONE
                } else {
                    imageview1.visibility = View.VISIBLE
                    imageview1.clipToOutline = true
                    if (boolean) {
                        glide.applyDefaultRequestOptions(RequestOptions())
                            .load(imageInt)
                            .into(imageview1)
                    } else
                        glide.applyDefaultRequestOptions(RequestOptions()).load(messageEntity.image)
                            .into(imageview1)
                }
            }
        }

        fun unbind() {
            avatar1.setImageDrawable(null)
            imageview1.setImageDrawable(null)
            imageview2.setImageDrawable(null)
        }
    }

}