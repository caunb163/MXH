package com.caunb163.mxh.ui.main.home.viewholder

import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import com.bumptech.glide.RequestManager
import com.caunb163.data.repository.RepositoryUser
import com.caunb163.domain.model.Post
import com.caunb163.domain.model.User
import com.caunb163.mxh.R
import com.caunb163.mxh.ui.main.home.HomeOnClick
import io.github.ponnamkarthik.richlinkpreview.RichLinkView
import io.github.ponnamkarthik.richlinkpreview.ViewListener
import java.util.regex.Pattern

class PostNoMediaViewHolder(view: View, glide: RequestManager) : PostBaseViewHolder(view, glide) {
    private var content: AppCompatTextView = view.findViewById(R.id.post_tv_content)
    private var richLinkView: RichLinkView = view.findViewById(R.id.post_richlinkview)

    override fun bind(post: Post, user: User, onHomeOnClick: HomeOnClick, repositoryUser: RepositoryUser) {
        bindView(post, user, onHomeOnClick, repositoryUser)
        content.text = post.content
        val temp = findExtractUrls(post.content)
        if (temp.isNotEmpty()) {
            richLinkView.visibility = View.VISIBLE
            richLinkView.setLink(temp[0], object : ViewListener{
                override fun onSuccess(status: Boolean) {}

                override fun onError(e: Exception?) {}
            })
        } else {
            richLinkView.visibility = View.GONE
        }
    }

    override fun unbind() {
        unbindView()
    }

    private fun findExtractUrls(text: String): List<String> {
        val containedUrls: MutableList<String> = ArrayList()
        val urlRegex =
            "((https?|ftp|gopher|telnet|file):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?+-=\\\\.&]*)"
        val pattern = Pattern.compile(urlRegex, Pattern.CASE_INSENSITIVE)
        val urlMatcher = pattern.matcher(text)
        while (urlMatcher.find()) {
            containedUrls.add(
                text.substring(
                    urlMatcher.start(0),
                    urlMatcher.end(0)
                )
            )
        }
        return containedUrls
    }
}