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

class PostImage2ViewHolder(view: View, private val glide: RequestManager) :
    PostBaseViewHolder(view, glide) {
    private var content: AppCompatTextView = view.findViewById(R.id.post_tv_content)
    private var richLinkView: RichLinkView = view.findViewById(R.id.post_richlinkview)
    private var img21: ImageView = view.findViewById(R.id.media_2_image_1)
    private var img22: ImageView = view.findViewById(R.id.media_2_image_2)

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
            .into(img21)
        glide.applyDefaultRequestOptions(RequestOptions()).load(post.images[1])
            .into(img22)
        img21.setOnClickListener { onHomeOnClick.onImageClick(post, 1) }
        img22.setOnClickListener { onHomeOnClick.onImageClick(post, 2) }
    }

    override fun unbind() {
        unbindView()
        img21.setImageDrawable(null)
        img22.setImageDrawable(null)
        img21.setOnClickListener(null)
        img22.setOnClickListener(null)
        glide.clear(img21)
        glide.clear(img22)
    }
}