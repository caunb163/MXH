package com.caunb163.mxh.ui.main.home.viewholder

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.caunb163.data.datalocal.LocalStorage
import com.caunb163.data.repository.RepositoryUser
import com.caunb163.domain.model.Post
import com.caunb163.domain.model.User
import com.caunb163.mxh.R
import com.caunb163.mxh.ui.main.home.HomeOnClick
import com.github.satoshun.coroutine.autodispose.view.autoDisposeScope
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

abstract class PostBaseViewHolder(view: View, requestManager: RequestManager) :
    RecyclerView.ViewHolder(view) {
    private var imgAvatar: CircleImageView = view.findViewById(R.id.post_imv_avatar)
    private var imvMenu: ImageView = view.findViewById(R.id.post_imv_menu)
    private var username: TextView = view.findViewById(R.id.post_tv_user)
    private var createDate: TextView = view.findViewById(R.id.post_tv_date)
    private var likeNumber: TextView = view.findViewById(R.id.post_tv_like_number)
    private var commentNumber: TextView = view.findViewById(R.id.post_tv_cmt_number)
    private var like: LinearLayout = view.findViewById(R.id.post_ll_like)
    private var comment: LinearLayout = view.findViewById(R.id.post_ll_cmt)
    private var share: LinearLayout = view.findViewById(R.id.post_ll_share)
    private var imvlike: ImageView = view.findViewById(R.id.post_ll_imv_like)
    private var tvlike: TextView = view.findViewById(R.id.post_ll_tv_like)
    private var header: ConstraintLayout = view.findViewById(R.id.post_header)
    private var context = view.context
    private var glide = requestManager

    abstract fun bind(
        post: Post,
        user: User,
        onHomeOnClick: HomeOnClick,
        repositoryUser: RepositoryUser
    )

    abstract fun unbind()

    @SuppressLint("SetTextI18n")
    fun bindView(
        post: Post,
        user: User,
        onHomeOnClick: HomeOnClick,
        repositoryUser: RepositoryUser
    ) {
        itemView.autoDisposeScope.launch {
            val mUser = repositoryUser.getUser(post.userId)
            username.text = mUser.username
            glide.applyDefaultRequestOptions(RequestOptions()).load(mUser.photoUrl).into(imgAvatar)
        }
        header.setOnClickListener { onHomeOnClick.onAvatarClick(post.userId) }
        if (post.isAds) {
            createDate.text = "Được tài trợ"
        } else {
            createDate.text = getDate(post.createDate)
        }

        likeNumber.text = post.arrLike.size.toString()
        if (post.arrLike.contains(user.userId)) {
            imvlike.setImageResource(R.drawable.ic_heart_red)
            tvlike.setTextColor(Color.RED)
        } else {
            imvlike.setImageResource(R.drawable.ic_heart)
            tvlike.setTextColor(Color.GRAY)
        }
        commentNumber.text = "${post.arrCmtId.size} bình luận"

        comment.setOnClickListener { onHomeOnClick.onCommentClick(post) }

        like.setOnClickListener { onHomeOnClick.onLikeClick(post.postId) }

        share.setOnClickListener { onHomeOnClick.onShareClick(post.content) }

        if (user.userId == post.userId) imvMenu.visibility = View.VISIBLE
        else imvMenu.visibility = View.INVISIBLE

        imvMenu.setOnClickListener {
            val popupMenu: PopupMenu = PopupMenu(context, imvMenu)
            popupMenu.inflate(R.menu.post_menu)
            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.post_edit -> {
                        onHomeOnClick.onEditClick(post)
                    }
                    R.id.post_delete -> {
                        onHomeOnClick.onDeleteClick(post)
                    }
                }
                false
            }
            popupMenu.show()
        }
    }

    fun unbindView() {
        imgAvatar.setImageDrawable(null)
        glide.clear(imgAvatar)
        comment.setOnClickListener(null)
        like.setOnClickListener(null)
        share.setOnClickListener(null)
        imvMenu.setOnClickListener(null)
    }

    private fun getDate(milliSeconds: Long): String? {
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = milliSeconds
        return formatter.format(calendar.time)
    }
}