package com.caunb163.mxh.ui.main.home.viewholder

import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatTextView
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.caunb163.domain.model.PostEntity
import com.caunb163.domain.model.User
import com.caunb163.mxh.R
import com.caunb163.mxh.ui.main.home.HomeOnClick
import io.github.ponnamkarthik.richlinkpreview.RichLinkView

class PostImage4ViewHolder(view: View, private val glide: RequestManager) :
    PostBaseViewHolder(view, glide) {
    private var content: AppCompatTextView = view.findViewById(R.id.post_tv_content)
    private var richLinkView: RichLinkView = view.findViewById(R.id.post_richlinkview)
    private var img41: ImageView = view.findViewById(R.id.media_4_image_1)
    private var img42: ImageView = view.findViewById(R.id.media_4_image_2)
    private var img43: ImageView = view.findViewById(R.id.media_4_image_3)
    private var img44: ImageView = view.findViewById(R.id.media_4_image_4)

    override fun bind(post: PostEntity, user: User, onHomeOnClick: HomeOnClick) {
        bindView(post, user, onHomeOnClick)
        richLinkView.visibility = View.GONE

        if (post.content.isNotEmpty()) {
            content.visibility = View.VISIBLE
            content.text = post.content
        } else {
            content.visibility = View.GONE
        }

        glide.applyDefaultRequestOptions(RequestOptions()).load(post.images[0])
            .into(img41)
        glide.applyDefaultRequestOptions(RequestOptions()).load(post.images[1])
            .into(img42)
        glide.applyDefaultRequestOptions(RequestOptions()).load(post.images[2])
            .into(img43)
        glide.applyDefaultRequestOptions(RequestOptions()).load(post.images[3])
            .into(img44)
    }

    override fun unbind() {
        unbindView()
        img41.setImageDrawable(null)
        img42.setImageDrawable(null)
        img43.setImageDrawable(null)
        img44.setImageDrawable(null)
        glide.clear(img41)
        glide.clear(img42)
        glide.clear(img43)
        glide.clear(img44)
    }
}