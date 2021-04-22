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

class PostVideo2ViewHolder(view: View, private val glide: RequestManager) :
    PostBaseViewHolder(view, glide) {
    private val TAG = "PostVideo2ViewHolder"
    private var content: AppCompatTextView = view.findViewById(R.id.post_tv_content)
    private var richLinkView: RichLinkView = view.findViewById(R.id.post_richlinkview)
    private var videoView: VideoView = view.findViewById(R.id.media_2_video_1)
    private var img22: ImageView = view.findViewById(R.id.media_2_video_2)

    override fun bind(post: PostEntity, user: User, onHomeOnClick: HomeOnClick) {
        bindView(post, user, onHomeOnClick)
        Log.e(TAG, "bind: PostVideo2ViewHolder" )
        richLinkView.visibility = View.GONE

        if (post.content.isNotEmpty()) {
            content.visibility = View.VISIBLE
            content.text = post.content
        } else {
            content.visibility = View.GONE
        }
        videoView.setVideoPath(post.video)
        videoView.start()

        glide.applyDefaultRequestOptions(RequestOptions()).load(post.images[0]).into(img22)
        img22.setOnClickListener { onHomeOnClick.onImageClick(post, 2) }
    }

    override fun unbind() {
        unbindView()
        img22.setImageDrawable(null)
        img22.setOnClickListener(null)
        glide.clear(img22)
    }

}