package com.caunb163.mxh.ui.main.video

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.caunb163.data.repository.RepositoryUser
import com.caunb163.domain.model.Post
import com.caunb163.mxh.R
import com.github.satoshun.coroutine.autodispose.view.autoDisposeScope
import de.hdodenhof.circleimageview.CircleImageView
import kohii.v1.core.Common
import kohii.v1.core.Playback
import kohii.v1.exoplayer.Kohii
import kotlinx.coroutines.launch

class VideoAdapter(
    private val kohii: Kohii,
    private val glide: RequestManager,
    private val repositoryUser: RepositoryUser
) : Adapter<VideoAdapter.VideoViewHolder>() {
    private var list: MutableList<Post> = mutableListOf()

    fun updateData(data: MutableList<Post>) {
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
        holder.bind(videoItem, repositoryUser)
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
        fun bind(post: Post, repositoryUser: RepositoryUser) {
            itemView.autoDisposeScope.launch {
                val mUser = repositoryUser.getUser(post.userId)
                username.text = mUser.username
                glide.applyDefaultRequestOptions(RequestOptions()).load(mUser.photoUrl)
                .into(avatar)
            }
            like.text = "${post.arrLike.size}M"
            comment.text = "${post.arrCmtId.size}k"
            content.text = post.content
        }

        fun unbind() {
            avatar.setImageDrawable(null)
            container.setOnClickListener(null)
            glide.clear(avatar)
        }
    }
}