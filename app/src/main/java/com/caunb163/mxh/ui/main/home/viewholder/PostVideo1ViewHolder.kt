package com.caunb163.mxh.ui.main.home.viewholder

import android.util.Log
import android.view.View
import android.widget.VideoView
import androidx.appcompat.widget.AppCompatTextView
import com.bumptech.glide.RequestManager
import com.caunb163.domain.model.PostEntity
import com.caunb163.domain.model.User
import com.caunb163.mxh.R
import com.caunb163.mxh.ui.main.home.HomeOnClick
import io.github.ponnamkarthik.richlinkpreview.RichLinkView

class PostVideo1ViewHolder(view: View, private val glide: RequestManager) :
    PostBaseViewHolder(view, glide) {
    private val TAG = "PostVideo1ViewHolder"
    private var content: AppCompatTextView = view.findViewById(R.id.post_tv_content)
    private var richLinkView: RichLinkView = view.findViewById(R.id.post_richlinkview)

    private var videoView: VideoView = view.findViewById(R.id.media_1_video)
    override fun bind(post: PostEntity, user: User, onHomeOnClick: HomeOnClick) {
        bindView(post, user, onHomeOnClick)
        richLinkView.visibility = View.GONE
        if (post.content.isNotEmpty()) {
            content.visibility = View.VISIBLE
            content.text = post.content
        } else {
            content.visibility = View.GONE
        }

        Log.e(TAG, "bind: PostVideo1ViewHolder" )
        videoView.setVideoPath(post.video)
        videoView.start()
    }

    override fun unbind() {
        unbindView()
    }

}