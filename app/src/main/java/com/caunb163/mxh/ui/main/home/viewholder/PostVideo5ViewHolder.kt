package com.caunb163.mxh.ui.main.home.viewholder

import android.annotation.SuppressLint
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.VideoView
import androidx.appcompat.widget.AppCompatTextView
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.caunb163.domain.model.PostEntity
import com.caunb163.domain.model.User
import com.caunb163.mxh.R
import com.caunb163.mxh.ui.main.home.HomeOnClick
import io.github.ponnamkarthik.richlinkpreview.RichLinkView

class PostVideo5ViewHolder(view: View, private val glide: RequestManager) :
    PostBaseViewHolder(view, glide) {
    private val TAG = "PostVideo5ViewHolder"
    private var content: AppCompatTextView = view.findViewById(R.id.post_tv_content)
    private var richLinkView: RichLinkView = view.findViewById(R.id.post_richlinkview)
    private var videoView: VideoView = view.findViewById(R.id.media_5_video_1)
    private var img52: ImageView = view.findViewById(R.id.media_5_video_2)
    private var img53: ImageView = view.findViewById(R.id.media_5_video_3)
    private var img54: ImageView = view.findViewById(R.id.media_5_video_4)
    private var img55: ImageView = view.findViewById(R.id.media_5_video_5)
    private var textView: TextView = view.findViewById(R.id.media_5_video_loadmore)

    @SuppressLint("SetTextI18n")
    override fun bind(post: PostEntity, user: User, onHomeOnClick: HomeOnClick) {
        bindView(post, user, onHomeOnClick)
        Log.e(TAG, "bind: PostVideo5ViewHolder" )
        richLinkView.visibility = View.GONE

        if (post.content.isNotEmpty()) {
            content.visibility = View.VISIBLE
            content.text = post.content
        } else {
            content.visibility = View.GONE
        }
        videoView.setVideoPath(post.video)
        videoView.start()

        glide.applyDefaultRequestOptions(RequestOptions()).load(post.images[0]).into(img52)
        glide.applyDefaultRequestOptions(RequestOptions()).load(post.images[0]).into(img53)
        glide.applyDefaultRequestOptions(RequestOptions()).load(post.images[0]).into(img54)
        glide.applyDefaultRequestOptions(RequestOptions()).load(post.images[0]).into(img55)

        img52.setOnClickListener { onHomeOnClick.onImageClick(post, 2) }
        img53.setOnClickListener { onHomeOnClick.onImageClick(post, 3) }
        img54.setOnClickListener { onHomeOnClick.onImageClick(post, 4) }
        img55.setOnClickListener { onHomeOnClick.onImageClick(post, 5) }

        if (post.images.size == 4) {
            textView.visibility = View.INVISIBLE
        } else {
            textView.visibility = View.VISIBLE
            textView.text = "+${post.images.size - 4}"
        }
    }

    override fun unbind() {
        unbindView()
        img52.setImageDrawable(null)
        img53.setImageDrawable(null)
        img54.setImageDrawable(null)
        img55.setImageDrawable(null)
        img52.setOnClickListener(null)
        img53.setOnClickListener(null)
        img54.setOnClickListener(null)
        img55.setOnClickListener(null)
        glide.clear(img52)
        glide.clear(img53)
        glide.clear(img54)
        glide.clear(img55)
    }
}