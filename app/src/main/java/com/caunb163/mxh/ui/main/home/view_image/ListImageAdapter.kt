package com.caunb163.mxh.ui.main.home.view_image

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.caunb163.domain.model.PostEntity
import com.caunb163.domain.model.User
import com.caunb163.mxh.R
import com.caunb163.mxh.ui.main.home.HomeOnClick
import com.caunb163.mxh.ui.main.home.PostViewHolder

class ListImageAdapter(
    private val glide: RequestManager,
    private val onClick: OnImageClick,
    private val user: User,
    private val homeOnClick: HomeOnClick,
    private val postEntity: PostEntity
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val TYPE_POST = 0
    private val TYPE_IMAGE = 1

//    private var list: MutableList<Any> = mutableListOf()
//
//    fun addData(datas: MutableList<Any>) {
//        list = datas
//        notifyDataSetChanged()
//    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_POST) {
            PostViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.layout_post_default, parent, false)
            )
        } else {
            ListImageViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.layout_image, parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.itemViewType == TYPE_POST) {
            val post = PostEntity(
                postId = postEntity.postId,
                userId = postEntity.userId,
                userName = postEntity.userName,
                userAvatar = postEntity.userAvatar,
                content = postEntity.content,
                createDate = postEntity.createDate,
                images = mutableListOf(),
                arrCmtId = postEntity.arrCmtId,
                arrLike = postEntity.arrLike
            )
            (holder as PostViewHolder).bind(glide, post, user, homeOnClick)
        } else {
            (holder as ListImageViewHolder).bind(postEntity.images[position - 1], onClick, glide)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            TYPE_POST
        } else TYPE_IMAGE
    }

    override fun getItemCount(): Int = postEntity.images.size + 1

    class ListImageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private var image: ImageView = view.findViewById(R.id.view_image)

        fun bind(uri: String, onClick: OnImageClick, glide: RequestManager) {
            glide.applyDefaultRequestOptions(RequestOptions()).load(uri).into(image)
            image.setOnClickListener {
                onClick.onClick(uri)
            }
        }

        fun unbind() {
            image.setImageDrawable(null)
            image.setOnClickListener(null)
        }
    }
}

interface OnImageClick {
    fun onClick(uri: String)
}