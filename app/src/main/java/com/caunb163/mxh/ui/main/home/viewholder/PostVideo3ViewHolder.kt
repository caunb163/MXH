package com.caunb163.mxh.ui.main.home.viewholder

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.VideoView
import androidx.appcompat.widget.AppCompatTextView
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.caunb163.domain.model.PostEntity
import com.caunb163.domain.model.User
import com.caunb163.mxh.R
import com.caunb163.mxh.ui.main.home.HomeOnClick
import io.github.ponnamkarthik.richlinkpreview.RichLinkView

class PostVideo3ViewHolder(view: View, private val glide: RequestManager) :
    PostBaseViewHolder(view, glide) {
    private val TAG = "PostVideo3ViewHolder"
    private var content: AppCompatTextView = view.findViewById(R.id.post_tv_content)
    private var richLinkView: RichLinkView = view.findViewById(R.id.post_richlinkview)
    private var videoView: VideoView = view.findViewById(R.id.media_3_video_1)
    private var img32: ImageView = view.findViewById(R.id.media_3_video_2)
    private var img33: ImageView = view.findViewById(R.id.media_3_video_3)

    override fun bind(post: PostEntity, user: User, onHomeOnClick: HomeOnClick) {
        bindView(post, user, onHomeOnClick)
        Log.e(TAG, "bind: PostVideo3ViewHolder" )
        richLinkView.visibility = View.GONE

        if (post.content.isNotEmpty()) {
            content.visibility = View.VISIBLE
            content.text = post.content
        } else {
            content.visibility = View.GONE
        }
        videoView.setVideoPath(post.video)
        videoView.start()

        glide.applyDefaultRequestOptions(RequestOptions()).load(post.images[0]).into(img32)
        glide.applyDefaultRequestOptions(RequestOptions()).load(post.images[1]).into(img33)
        img32.setOnClickListener { onHomeOnClick.onImageClick(post, 2) }
        img33.setOnClickListener { onHomeOnClick.onImageClick(post, 3) }

    }

    override fun unbind() {
        unbindView()
        img32.setImageDrawable(null)
        img33.setImageDrawable(null)
        img32.setOnClickListener(null)
        img33.setOnClickListener(null)
        glide.clear(img32)
        glide.clear(img33)
    }
}