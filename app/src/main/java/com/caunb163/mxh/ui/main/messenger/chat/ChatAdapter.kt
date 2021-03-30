package com.caunb163.mxh.ui.main.messenger.chat

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.ViewFlipper
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.caunb163.domain.model.MessageEntity
import com.caunb163.domain.model.User
import com.caunb163.mxh.R
import de.hdodenhof.circleimageview.CircleImageView

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
        private val avatar2: CircleImageView = view.findViewById(R.id.chat_avatar2)
        private val message1: TextView = view.findViewById(R.id.chat_message1)
        private val message2: TextView = view.findViewById(R.id.chat_message2)

        fun bind(messageEntity: MessageEntity, user: User, glide: RequestManager) {
            if (TextUtils.equals(messageEntity.userId, user.userId)) {
                viewFlipper.displayedChild = 1
                glide.applyDefaultRequestOptions(RequestOptions()).load(messageEntity.userAvatar)
                    .into(avatar2)
                message2.text = messageEntity.content
            } else {
                viewFlipper.displayedChild = 0
                glide.applyDefaultRequestOptions(RequestOptions()).load(messageEntity.userAvatar)
                    .into(avatar1)
                message1.text = messageEntity.content
            }
        }

        fun unbind() {
            avatar1.setImageDrawable(null)
            avatar2.setImageDrawable(null)
        }
    }

}