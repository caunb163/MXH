package com.caunb163.mxh.ui.main.home.view_image

import android.content.Context
import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.MediaController
import android.widget.VideoView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.caunb163.domain.model.PostEntity
import com.caunb163.domain.model.User
import com.caunb163.mxh.R
import com.caunb163.mxh.ui.main.home.HomeOnClick
import com.caunb163.mxh.ui.main.home.PostViewHolder
import com.caunb163.mxh.ui.main.home.viewholder.PostNoMediaViewHolder

class ListImageAdapter(
    private val glide: RequestManager,
    private val onClick: OnImageClick,
    private val user: User,
    private val homeOnClick: HomeOnClick,
    private val postEntity: PostEntity
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
                val post = PostEntity(
                    postId = postEntity.postId,
                    userId = postEntity.userId,
                    userName = postEntity.userName,
                    userAvatar = postEntity.userAvatar,
                    content = postEntity.content,
                    createDate = postEntity.createDate,
                    images = mutableListOf(),
                    arrCmtId = postEntity.arrCmtId,
                    arrLike = postEntity.arrLike,
                    video = ""
                )
                (holder as PostNoMediaViewHolder).bind(post, user, homeOnClick)
            }

            TYPE_VIDEO -> (holder as VideoViewHolder).bind(postEntity, onClick, position - 1)

            TYPE_IMAGE -> (holder as ListImageViewHolder).bind(postEntity, onClick, position - 1, postEntity.video.isNotEmpty())

        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (postEntity.video.isNotEmpty()) {
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
        return if (postEntity.video.isNotEmpty()) {
            postEntity.images.size + 2
        } else
            postEntity.images.size + 1
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        super.onViewRecycled(holder)
        when (holder) {
            is PostViewHolder -> holder.unbind()
            is ListImageViewHolder -> holder.unbind()
        }
    }

    class ListImageViewHolder(view: View, private val glide: RequestManager) :
        RecyclerView.ViewHolder(view) {
        private var image: ImageView = view.findViewById(R.id.view_image)

        fun bind(postEntity: PostEntity, onClick: OnImageClick, position: Int, boolean: Boolean) {
            val list: MutableList<String> = mutableListOf()
            if (boolean){
                glide.applyDefaultRequestOptions(RequestOptions()).load(postEntity.images[position - 1]).into(image)
                list.add(postEntity.video)
            } else {
                glide.applyDefaultRequestOptions(RequestOptions()).load(postEntity.images[position]).into(image)
            }

            list.addAll(postEntity.images)
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
//        private var videoView: VideoView = view.findViewById(R.id.view_video)
        private var imageView: ImageView = view.findViewById(R.id.view_video)

        fun bind(postEntity: PostEntity, onClick: OnImageClick, position: Int) {
            glide.applyDefaultRequestOptions(RequestOptions()).load(postEntity.video).into(imageView)
//            videoView.setVideoPath(postEntity.video)
//            val mediaController = MediaController(context)
//            mediaController.setAnchorView(videoView)
//            videoView.setMediaController(mediaController)
//            videoView.start()
            val list: MutableList<String> = mutableListOf()
            list.add(postEntity.video)
            list.addAll(postEntity.images)
            imageView.setOnClickListener { onClick.onViewClick(list, position, true) }
        }
    }
}

interface OnImageClick {
    fun onViewClick(listMedia: MutableList<String>, position: Int, boolean: Boolean)
}