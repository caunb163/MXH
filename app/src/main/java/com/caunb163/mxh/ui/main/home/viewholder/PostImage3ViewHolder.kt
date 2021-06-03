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

class PostImage3ViewHolder(view: View, private val glide: RequestManager) :
    PostBaseViewHolder(view, glide) {
    private var content: AppCompatTextView = view.findViewById(R.id.post_tv_content)
    private var richLinkView: RichLinkView = view.findViewById(R.id.post_richlinkview)
    private var img31: ImageView = view.findViewById(R.id.media_3_image_1)
    private var img32: ImageView = view.findViewById(R.id.media_3_image_2)
    private var img33: ImageView = view.findViewById(R.id.media_3_image_3)

    override fun bind(post: Post, user: User, onHomeOnClick: HomeOnClick, repositoryUser: RepositoryUser) {
        bindView(post, user, onHomeOnClick, repositoryUser)
        richLinkView.visibility = View.GONE

        if (post.content.isNotEmpty()) {
            content.visibility = View.VISIBLE
            content.text = post.content
        } else {
            content.visibility = View.GONE
        }

        glide.applyDefaultRequestOptions(RequestOptions()).load(post.images[0])
            .into(img31)
        glide.applyDefaultRequestOptions(RequestOptions()).load(post.images[1])
            .into(img32)
        glide.applyDefaultRequestOptions(RequestOptions()).load(post.images[2])
            .into(img33)
        img31.setOnClickListener { onHomeOnClick.onImageClick(post, 1) }
        img32.setOnClickListener { onHomeOnClick.onImageClick(post, 2) }
        img33.setOnClickListener { onHomeOnClick.onImageClick(post, 3) }
    }

    override fun unbind() {
        unbindView()
        img31.setImageDrawable(null)
        img32.setImageDrawable(null)
        img33.setImageDrawable(null)
        img31.setOnClickListener(null)
        img32.setOnClickListener(null)
        img33.setOnClickListener(null)
        glide.clear(img31)
        glide.clear(img32)
        glide.clear(img33)
    }

}