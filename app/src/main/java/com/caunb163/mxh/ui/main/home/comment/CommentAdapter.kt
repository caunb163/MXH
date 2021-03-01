package com.caunb163.mxh.ui.main.home.comment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.caunb163.domain.model.Comment
import com.caunb163.domain.model.CommentEntity
import com.caunb163.domain.model.User
import com.caunb163.mxh.R
import de.hdodenhof.circleimageview.CircleImageView

class CommentAdapter(
    private val glide: RequestManager,
    private val user: User
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var list: MutableList<CommentEntity> = mutableListOf()

    fun updateData(data: MutableList<CommentEntity>) {
        list = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return CommentViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.layout_comment, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as CommentViewHolder).bind(glide, user, list[position])
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        super.onViewRecycled(holder)
        (holder as CommentViewHolder).unbind()
    }

    override fun getItemCount(): Int = list.size

    class CommentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private var imgAvatar: CircleImageView = view.findViewById(R.id.cmt_user_avatar)
        private var tvUserName: TextView = view.findViewById(R.id.cmt_tv_username)
        private var tvContent: TextView = view.findViewById(R.id.cmt_tv_content)
        private var imageview: ImageView = view.findViewById(R.id.cmt_image)

        fun bind(glide: RequestManager, user: User, comment: CommentEntity) {
            glide.applyDefaultRequestOptions(RequestOptions()).load(comment.userAvatar)
                .into(imgAvatar)
            glide.applyDefaultRequestOptions(RequestOptions()).load(comment.image).into(imageview)
            tvUserName.text = comment.username
            tvContent.text = comment.content
        }

        fun unbind() {
            imgAvatar.setImageDrawable(null)
            imageview.setImageDrawable(null)
        }
    }
}