package com.caunb163.mxh.ui.main.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.caunb163.domain.model.Post
import com.caunb163.domain.model.PostEntity
import com.caunb163.domain.model.User
import com.caunb163.mxh.R
import de.hdodenhof.circleimageview.CircleImageView

class HomeAdapter(
    private val glide: RequestManager,
    private val homeOnClick: HomeOnClick,
    private val user: User
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TYPE_CREATE_POST = 0
    private val TYPE_POST = 1

    private var list: MutableList<Any> = mutableListOf()

    fun updateData(datas: MutableList<Any>) {
        list = datas
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_CREATE_POST) {
            CreatePostViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.layout_create_post, parent, false)
            )
        } else PostViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.layout_post_default, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.itemViewType == TYPE_POST) {
            (holder as PostViewHolder).bind(glide, list[position] as PostEntity, user, homeOnClick)
        } else {
            (holder as CreatePostViewHolder).bind(glide, list[position] as User, homeOnClick)
        }
    }

    override fun getItemViewType(position: Int): Int {
//        return if (position == 0) TYPE_CREATE_POST else TYPE_POST
        return if (list[position] is User) {
            TYPE_CREATE_POST
        } else TYPE_POST
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        super.onViewRecycled(holder)
        if (holder is CreatePostViewHolder) holder.unbind()
        else if (holder is PostViewHolder) holder.unbind()

    }

    class CreatePostViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private var imgAvatar: CircleImageView = view.findViewById(R.id.createpost_avatar)
        private var tvCreatePost: TextView = view.findViewById(R.id.createpost_textview)

        fun bind(glide: RequestManager, user: User, onClick: HomeOnClick) {
            glide.applyDefaultRequestOptions(RequestOptions()).load(user.photoUrl).into(imgAvatar)
            tvCreatePost.setOnClickListener {
                onClick.createPostClick()
            }
        }

        fun unbind() {
            imgAvatar.setImageDrawable(null)
            tvCreatePost.setOnClickListener(null)
        }
    }
}

interface HomeOnClick {
    fun createPostClick()

    fun onCommentClick(post: PostEntity)

    fun onLikeClick(postId: String)

    fun onShareClick(content: String)

    fun onEditClick(post: PostEntity)

    fun onDeleteClick(post: PostEntity)

}