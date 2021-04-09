package com.caunb163.mxh.ui.main.messenger.chat.emoji

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.caunb163.mxh.R

class EmojiAdapter(
    private val list: Array<Int>,
    private val glide: RequestManager,
    private val listener: OnEmojiClick
) : RecyclerView.Adapter<EmojiAdapter.EmojiViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmojiViewHolder {
        return EmojiViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.layout_emoji, parent, false)
        )
    }

    override fun onBindViewHolder(holder: EmojiViewHolder, position: Int) {
        holder.bind(list[position], glide, listener)
    }

    override fun onViewRecycled(holder: EmojiViewHolder) {
        super.onViewRecycled(holder)
        holder.unbind()
    }

    override fun getItemCount(): Int = list.size

    class EmojiViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val TAG = "EmojiAdapter"
        private val imageView: ImageView = view.findViewById(R.id.emoji_image)

        fun bind(url: Int, glide: RequestManager, onEmojiClick: OnEmojiClick) {
            glide.applyDefaultRequestOptions(RequestOptions()).load(url).into(imageView)
            imageView.setOnClickListener {
                onEmojiClick.onGifClick(url)
            }
        }

        fun unbind() {
            imageView.setImageDrawable(null)
            imageView.setOnClickListener(null)
        }
    }
}

interface OnEmojiClick {
    fun onGifClick(url: Int)
}