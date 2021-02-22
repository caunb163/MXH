package com.caunb163.mxh.ui.main.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.caunb163.domain.model.Post
import com.caunb163.domain.model.User
import com.caunb163.mxh.R
import de.hdodenhof.circleimageview.CircleImageView

class HomeAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val TYPE_CREATE_POST = 0
    private val TYPE_POST = 1

    private var list: MutableList<Any> = mutableListOf()

    fun updateData(datas: MutableList<Any>) {
        list = datas
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
//        val view =
//            LayoutInflater.from(parent.context).inflate(R.layout.layout_post_default, parent, false)
//        return HomeViewHolder(view)
        return if (viewType == TYPE_CREATE_POST) {
            CreatePostViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.layout_create_post, parent, false)
            )
        } else HomeViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.layout_post_default, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.itemViewType == TYPE_POST) {
            (holder as HomeViewHolder).bind(list[position] as Post)
        } else {
            (holder as CreatePostViewHolder).bind(list[position] as User)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) TYPE_CREATE_POST else TYPE_POST
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class HomeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private var imgAvatar: CircleImageView = view.findViewById(R.id.post_imv_avatar)
        private var username: TextView = view.findViewById(R.id.post_tv_user)
        private var createDate: TextView = view.findViewById(R.id.post_tv_date)
        private var content: TextView = view.findViewById(R.id.post_tv_content)
        private var likeNumber: TextView = view.findViewById(R.id.post_tv_like_number)
        private var commentNumber: TextView = view.findViewById(R.id.post_tv_cmt_number)
        private var like: LinearLayout = view.findViewById(R.id.post_ll_like)
        private var comment: LinearLayout = view.findViewById(R.id.post_ll_cmt)
        private var share: LinearLayout = view.findViewById(R.id.post_ll_share)

        @SuppressLint("SetTextI18n")
        fun bind(post: Post) {
            username.text = post.userName
            createDate.text = post.createDate
            content.text = post.content
            likeNumber.text = post.likeNumber.toString()
            commentNumber.text = "${post.commentNumber} bình luận"
        }
    }

    class CreatePostViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private var imgAvatar: CircleImageView = view.findViewById(R.id.createpost_avatar)

        fun bind(user: User) {}
    }
}