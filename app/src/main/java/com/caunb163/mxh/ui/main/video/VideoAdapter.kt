package com.caunb163.mxh.ui.main.video

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.VideoView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.caunb163.domain.model.PostEntity
import com.caunb163.mxh.R
import com.google.android.exoplayer2.ui.PlayerView
import de.hdodenhof.circleimageview.CircleImageView
import kohii.v1.core.Common
import kohii.v1.core.Playback
import kohii.v1.exoplayer.Kohii

class VideoAdapter(
    private val kohii: Kohii,
    private val glide: RequestManager
) : Adapter<VideoAdapter.VideoViewHolder>() {
    private var list: MutableList<PostEntity> = mutableListOf()

    fun updateData(data: MutableList<PostEntity>) {
        list = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        return VideoViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.layout_item_video, parent, false), glide)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        val videoItem = list[position]
        kohii.setUp(videoItem.video) {
            tag = "${videoItem.createDate}"
            preload = true
            repeatMode = Common.REPEAT_MODE_ONE
            controller = object : Playback.Controller {
                override fun kohiiCanStart(): Boolean = true
                override fun kohiiCanPause(): Boolean = true
                override fun setupRenderer(playback: Playback, renderer: Any?) {
                    holder.container.setOnClickListener {
                        val playable = playback.playable ?: return@setOnClickListener
                        if (playable.isPlaying()) {
                            playback.manager.pause(playable)
                            holder.imagePlay.visibility = View.VISIBLE
                        } else {
                            playback.manager.play(playable)
                            holder.imagePlay.visibility = View.INVISIBLE
                        }
                    }
                }

                override fun teardownRenderer(playback: Playback, renderer: Any?) {
                    holder.container.setOnClickListener(null)
                }
            }
        }.bind(holder.playerView)
        holder.bind(videoItem)
    }

    override fun onViewRecycled(holder: VideoViewHolder) {
        super.onViewRecycled(holder)
        holder.unbind()
    }

    override fun getItemCount(): Int = list.size

    class VideoViewHolder(view: View, private val glide: RequestManager) :
        RecyclerView.ViewHolder(view) {
        var container: ViewGroup = view.findViewById(R.id.video_container)
        var playerView: ViewGroup = view.findViewById(R.id.video_videoView)
        var imagePlay: ImageView = view.findViewById(R.id.video_play)
        private var avatar: CircleImageView = view.findViewById(R.id.video_avatar)
        private var like: TextView = view.findViewById(R.id.video_like)
        private var comment: TextView = view.findViewById(R.id.video_comment)
        private var username: TextView = view.findViewById(R.id.video_user)
        private var content: TextView = view.findViewById(R.id.video_content)

        @SuppressLint("SetTextI18n")
        fun bind(postEntity: PostEntity) {
            glide.applyDefaultRequestOptions(RequestOptions()).load(postEntity.userAvatar)
                .into(avatar)
            like.text = "${postEntity.arrLike.size}M"
            comment.text = "${postEntity.arrCmtId.size}k"
            username.text = postEntity.userName
            content.text = postEntity.content
        }

        fun unbind() {
            avatar.setImageDrawable(null)
            container.setOnClickListener(null)
            glide.clear(avatar)
        }
    }
}