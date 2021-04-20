package com.caunb163.mxh.ui.main.home.viewholder

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.caunb163.domain.model.User
import com.caunb163.mxh.R
import com.caunb163.mxh.ui.main.home.HomeOnClick
import de.hdodenhof.circleimageview.CircleImageView

class CreatePostViewHolder(view: View, private val requestManager: RequestManager) :
    RecyclerView.ViewHolder(view) {
    private var imgAvatar: CircleImageView = view.findViewById(R.id.createpost_avatar)
    private var tvCreatePost: TextView = view.findViewById(R.id.createpost_textview)
    private val glide = requestManager

    fun bind(user: User, onClick: HomeOnClick) {
        glide.applyDefaultRequestOptions(RequestOptions()).load(user.photoUrl).into(imgAvatar)
        tvCreatePost.setOnClickListener {
            onClick.createPostClick()
        }
    }

    fun unbind() {
        imgAvatar.setImageDrawable(null)
        glide.clear(imgAvatar)
        tvCreatePost.setOnClickListener(null)
    }
}