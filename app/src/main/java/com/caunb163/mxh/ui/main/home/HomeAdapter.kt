package com.caunb163.mxh.ui.main.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.caunb163.domain.model.PostEntity
import com.caunb163.domain.model.User
import com.caunb163.mxh.R
import com.caunb163.mxh.ui.main.home.viewholder.*

class HomeAdapter(
    private val glide: RequestManager,
    private val homeOnClick: HomeOnClick,
    private val user: User,
    private val context: Context
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TYPE_CREATE_POST = 0
    private val TYPE_POST_NO_MEDIA = 1
    private val TYPE_POST_IMAGE_1 = 2
    private val TYPE_POST_IMAGE_2 = 3
    private val TYPE_POST_IMAGE_3 = 4
    private val TYPE_POST_IMAGE_4 = 5
    private val TYPE_POST_IMAGE_5 = 6
    private val TYPE_POST_VIDEO_1 = 7
    private val TYPE_POST_VIDEO_2 = 8
    private val TYPE_POST_VIDEO_3 = 9
    private val TYPE_POST_VIDEO_4 = 10
    private val TYPE_POST_VIDEO_5 = 11

    private var list: MutableList<Any> = mutableListOf()

    fun updateData(datas: MutableList<Any>) {
        list = datas
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            TYPE_POST_NO_MEDIA -> return PostNoMediaViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.layout_post_no_media, parent, false), glide)

            TYPE_POST_IMAGE_1 -> return PostImage1ViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.layout_post_1_image, parent, false), glide)

            TYPE_POST_IMAGE_2 -> return PostImage2ViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.layout_post_2_image, parent, false), glide)

            TYPE_POST_IMAGE_3 -> return PostImage3ViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.layout_post_3_image, parent, false), glide)

            TYPE_POST_IMAGE_4 -> return PostImage4ViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.layout_post_4_image, parent, false), glide)

            TYPE_POST_IMAGE_5 -> return PostImage5ViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.layout_post_5_image, parent, false), glide)

            TYPE_POST_VIDEO_1 -> return PostVideo1ViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.layout_post_1_video, parent, false), glide)

            TYPE_POST_VIDEO_2 -> return PostVideo2ViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.layout_post_2_video, parent, false), glide)

            TYPE_POST_VIDEO_3 -> return PostVideo3ViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.layout_post_3_video, parent, false), glide)

            TYPE_POST_VIDEO_4 -> return PostVideo4ViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.layout_post_4_video, parent, false), glide)

            TYPE_POST_VIDEO_5 -> return PostVideo5ViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.layout_post_5_video, parent, false), glide)

            else -> return CreatePostViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.layout_create_post, parent, false), glide)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            TYPE_CREATE_POST -> (holder as CreatePostViewHolder).bind(list[position] as User, homeOnClick)
            TYPE_POST_NO_MEDIA -> (holder as PostNoMediaViewHolder).bind(list[position] as PostEntity, user, homeOnClick)

            TYPE_POST_IMAGE_1 -> (holder as PostImage1ViewHolder).bind(list[position] as PostEntity, user, homeOnClick)
            TYPE_POST_IMAGE_2 -> (holder as PostImage2ViewHolder).bind(list[position] as PostEntity, user, homeOnClick)
            TYPE_POST_IMAGE_3 -> (holder as PostImage3ViewHolder).bind(list[position] as PostEntity, user, homeOnClick)
            TYPE_POST_IMAGE_4 -> (holder as PostImage4ViewHolder).bind(list[position] as PostEntity, user, homeOnClick)
            TYPE_POST_IMAGE_5 -> (holder as PostImage5ViewHolder).bind(list[position] as PostEntity, user, homeOnClick)

            TYPE_POST_VIDEO_1 -> (holder as PostVideo1ViewHolder).bind(list[position] as PostEntity, user, homeOnClick)
            TYPE_POST_VIDEO_2 -> (holder as PostVideo2ViewHolder).bind(list[position] as PostEntity, user, homeOnClick)
            TYPE_POST_VIDEO_3 -> (holder as PostVideo3ViewHolder).bind(list[position] as PostEntity, user, homeOnClick)
            TYPE_POST_VIDEO_4 -> (holder as PostVideo4ViewHolder).bind(list[position] as PostEntity, user, homeOnClick)
            TYPE_POST_VIDEO_5 -> (holder as PostVideo5ViewHolder).bind(list[position] as PostEntity, user, homeOnClick)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            list[position] is PostEntity -> {
                val post = list[position] as PostEntity
                if (post.video.isNotEmpty()) {
                    when (post.images.size) {
                        0 -> TYPE_POST_VIDEO_1
                        1 -> TYPE_POST_VIDEO_2
                        2 -> TYPE_POST_VIDEO_3
                        3 -> TYPE_POST_VIDEO_4
                        else -> TYPE_POST_VIDEO_5
                    }
                } else {
                    when (post.images.size) {
                        0 -> TYPE_POST_NO_MEDIA
                        1 -> TYPE_POST_IMAGE_1
                        2 -> TYPE_POST_IMAGE_2
                        3 -> TYPE_POST_IMAGE_3
                        4 -> TYPE_POST_IMAGE_4
                        else -> TYPE_POST_IMAGE_5
                    }
                }
            }
            else -> TYPE_CREATE_POST
        }
    }

    override fun getItemCount(): Int = list.size

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        super.onViewRecycled(holder)
        when (holder) {
            is CreatePostViewHolder -> holder.unbind()
            is PostNoMediaViewHolder -> holder.unbind()
            is PostImage1ViewHolder -> holder.unbind()
            is PostImage2ViewHolder -> holder.unbind()
            is PostImage3ViewHolder -> holder.unbind()
            is PostImage4ViewHolder -> holder.unbind()
            is PostImage5ViewHolder -> holder.unbind()
            is PostVideo1ViewHolder -> holder.unbind()
            is PostVideo2ViewHolder -> holder.unbind()
            is PostVideo3ViewHolder -> holder.unbind()
            is PostVideo4ViewHolder -> holder.unbind()
            is PostVideo5ViewHolder -> holder.unbind()
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

    fun onImageClick(post: PostEntity, position: Int)
}