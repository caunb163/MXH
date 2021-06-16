package com.caunb163.mxh.ui.main.home.view_image

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.caunb163.data.repository.RepositoryUser
import com.caunb163.domain.model.Post
import com.caunb163.domain.model.User
import com.caunb163.mxh.R
import com.caunb163.mxh.ui.main.home.HomeOnClick
import com.caunb163.mxh.ui.main.home.viewholder.PostNoMediaViewHolder

class ListImageAdapter(
    private val glide: RequestManager,
    private val onClick: OnImageClick,
    private val user: User,
    private val homeOnClick: HomeOnClick,
    private val post: Post,
    private val repositoryUser: RepositoryUser
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val TYPE_POST = 0
    private val TYPE_IMAGE = 1
    private val TYPE_VIDEO = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_POST -> PostNoMediaViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.layout_post_no_media, parent, false), glide)

            TYPE_VIDEO -> VideoViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.layout_video, parent, false), glide)

            else -> ListImageViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.layout_image, parent, false), glide)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            TYPE_POST -> {
                (holder as PostNoMediaViewHolder).bind(post, user, homeOnClick, repositoryUser)
            }

            TYPE_VIDEO -> (holder as VideoViewHolder).bind(post, onClick, position - 1)

            TYPE_IMAGE -> (holder as ListImageViewHolder).bind(post, onClick, position - 1, post.video.isNotEmpty())

        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (post.video.isNotEmpty()) {
            when (position) {
                0 -> TYPE_POST
                1 -> TYPE_VIDEO
                else -> TYPE_IMAGE
            }
        } else {
            when (position) {
                0 -> TYPE_POST
                else -> TYPE_IMAGE
            }
        }
    }

    override fun getItemCount(): Int {
        return if (post.video.isNotEmpty()) {
            post.images.size + 2
        } else
            post.images.size + 1
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        super.onViewRecycled(holder)
        when (holder) {
            is PostNoMediaViewHolder -> holder.unbind()
            is ListImageViewHolder -> holder.unbind()
        }
    }

    class ListImageViewHolder(view: View, private val glide: RequestManager) :
        RecyclerView.ViewHolder(view) {
        private var image: ImageView = view.findViewById(R.id.view_image)

        fun bind(post: Post, onClick: OnImageClick, position: Int, boolean: Boolean) {
            val list: MutableList<String> = mutableListOf()
            if (boolean){
                glide.applyDefaultRequestOptions(RequestOptions()).load(post.images[position - 1]).into(image)
                list.add(post.video)
            } else {
                glide.applyDefaultRequestOptions(RequestOptions()).load(post.images[position]).into(image)
            }

            list.addAll(post.images)
            image.setOnClickListener { onClick.onViewClick(list, position, boolean) }
        }

        fun unbind() {
            image.setImageDrawable(null)
            image.setOnClickListener(null)
            glide.clear(image)
        }
    }

    class VideoViewHolder(view: View, private val glide: RequestManager) :
        RecyclerView.ViewHolder(view) {
        private var imageView: ImageView = view.findViewById(R.id.view_video)

        fun bind(post: Post, onClick: OnImageClick, position: Int) {
            glide.applyDefaultRequestOptions(RequestOptions()).load(post.video).into(imageView)
            val list: MutableList<String> = mutableListOf()
            list.add(post.video)
            list.addAll(post.images)
            imageView.setOnClickListener { onClick.onViewClick(list, position, true) }
        }
    }
}

interface OnImageClick {
    fun onViewClick(listMedia: MutableList<String>, position: Int, boolean: Boolean)
}