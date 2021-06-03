package com.caunb163.mxh.ui.main.home.viewholder

import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatTextView
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.caunb163.data.repository.RepositoryUser
import com.caunb163.domain.model.Post
import com.caunb163.domain.model.User
import com.caunb163.mxh.R
import com.caunb163.mxh.ui.main.home.HomeOnClick
import io.github.ponnamkarthik.richlinkpreview.RichLinkView

class PostVideo1ViewHolder(
    view: View,
    private val glide: RequestManager
) : PostBaseViewHolder(view, glide) {
    private val TAG = "PostVideo1ViewHolder"
    private var content: AppCompatTextView = view.findViewById(R.id.post_tv_content)
    private var richLinkView: RichLinkView = view.findViewById(R.id.post_richlinkview)
    private var img1: ImageView = view.findViewById(R.id.media_1_video)

    override fun bind(post: Post, user: User, onHomeOnClick: HomeOnClick, repositoryUser: RepositoryUser) {
        bindView(post, user, onHomeOnClick, repositoryUser)
        richLinkView.visibility = View.GONE
        if (post.content.isNotEmpty()) {
            content.visibility = View.VISIBLE
            content.text = post.content
        } else {
            content.visibility = View.GONE
        }

        glide.applyDefaultRequestOptions(RequestOptions()).load(post.video).into(img1)
        img1.setOnClickListener { onHomeOnClick.onImageClick(post, 1) }
    }

    override fun unbind() {
        unbindView()
        img1.setImageDrawable(null)
        img1.setOnClickListener(null)
        glide.clear(img1)
    }

}