package com.caunb163.mxh.ui.main.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.caunb163.domain.model.Post
import com.caunb163.domain.model.User
import com.caunb163.mxh.R
import de.hdodenhof.circleimageview.CircleImageView
import java.text.SimpleDateFormat
import java.util.*

class HomeAdapter(
    private val glide: RequestManager,
    private val homeOnClick: HomeOnClick,
    private val user: User
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TYPE_CREATE_POST = 0
    private val TYPE_POST = 1

    private var list: MutableList<Any> = mutableListOf()

    fun updateData(datas: MutableList<Any>) {
        list = datas
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
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
            (holder as HomeViewHolder).bind(glide, list[position] as Post, user)
        } else {
            (holder as CreatePostViewHolder).bind(glide, list[position] as User, homeOnClick)
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
        private var imvMenu: ImageView = view.findViewById(R.id.post_imv_menu)
        private var username: TextView = view.findViewById(R.id.post_tv_user)
        private var createDate: TextView = view.findViewById(R.id.post_tv_date)
        private var content: TextView = view.findViewById(R.id.post_tv_content)
        private var likeNumber: TextView = view.findViewById(R.id.post_tv_like_number)
        private var commentNumber: TextView = view.findViewById(R.id.post_tv_cmt_number)
        private var like: LinearLayout = view.findViewById(R.id.post_ll_like)
        private var comment: LinearLayout = view.findViewById(R.id.post_ll_cmt)
        private var share: LinearLayout = view.findViewById(R.id.post_ll_share)

        private var ctl1: ConstraintLayout = view.findViewById(R.id.post_ctl1)
        private var ctl2: ConstraintLayout = view.findViewById(R.id.post_ctl2)
        private var ctl3: ConstraintLayout = view.findViewById(R.id.post_ctl3)
        private var ctl4: ConstraintLayout = view.findViewById(R.id.post_ctl4)
        private var ctl5: ConstraintLayout = view.findViewById(R.id.post_ctl5)

        private var img1: ImageView = view.findViewById(R.id.post_1_img)

        private var img21: ImageView = view.findViewById(R.id.post_2_img1)
        private var img22: ImageView = view.findViewById(R.id.post_2_img2)

        private var img31: ImageView = view.findViewById(R.id.post_3_img1)
        private var img32: ImageView = view.findViewById(R.id.post_3_img2)
        private var img33: ImageView = view.findViewById(R.id.post_3_img3)

        private var img41: ImageView = view.findViewById(R.id.post_4_img1)
        private var img42: ImageView = view.findViewById(R.id.post_4_img2)
        private var img43: ImageView = view.findViewById(R.id.post_4_img3)
        private var img44: ImageView = view.findViewById(R.id.post_4_img4)

        private var img51: ImageView = view.findViewById(R.id.post_5_img1)
        private var img52: ImageView = view.findViewById(R.id.post_5_img2)
        private var img53: ImageView = view.findViewById(R.id.post_5_img3)
        private var img54: ImageView = view.findViewById(R.id.post_5_img4)
        private var img55: ImageView = view.findViewById(R.id.post_5_img5)
        private var tvImgNumber: TextView = view.findViewById(R.id.post_tv_img_number)

        @SuppressLint("SetTextI18n")
        fun bind(glide: RequestManager, post: Post, user: User) {
            username.text = post.userName
            createDate.text = getDate(post.createDate, "dd/MM/yyyy")
            content.text = post.content
            likeNumber.text = post.likeNumber.toString()
            commentNumber.text = "${post.commentNumber} bình luận"

            glide.applyDefaultRequestOptions(RequestOptions()).load(post.userAvatar).into(imgAvatar)
            when (post.images.size) {
                0 -> {
                    showImage(0)
                }

                1 -> {
                    showImage(1)
                    glide.applyDefaultRequestOptions(RequestOptions()).load(post.images[0])
                        .into(img1)
                }

                2 -> {
                    showImage(2)
                    glide.applyDefaultRequestOptions(RequestOptions()).load(post.images[0])
                        .into(img21)
                    glide.applyDefaultRequestOptions(RequestOptions()).load(post.images[1])
                        .into(img22)
                }

                3 -> {
                    showImage(3)
                    glide.applyDefaultRequestOptions(RequestOptions()).load(post.images[0])
                        .into(img31)
                    glide.applyDefaultRequestOptions(RequestOptions()).load(post.images[1])
                        .into(img32)
                    glide.applyDefaultRequestOptions(RequestOptions()).load(post.images[2])
                        .into(img33)
                }

                4 -> {
                    showImage(4)
                    glide.applyDefaultRequestOptions(RequestOptions()).load(post.images[0])
                        .into(img41)
                    glide.applyDefaultRequestOptions(RequestOptions()).load(post.images[1])
                        .into(img42)
                    glide.applyDefaultRequestOptions(RequestOptions()).load(post.images[2])
                        .into(img43)
                    glide.applyDefaultRequestOptions(RequestOptions()).load(post.images[3])
                        .into(img44)
                }

                else -> {
                    showImage(5)
                    glide.applyDefaultRequestOptions(RequestOptions()).load(post.images[0])
                        .into(img51)
                    glide.applyDefaultRequestOptions(RequestOptions()).load(post.images[1])
                        .into(img52)
                    glide.applyDefaultRequestOptions(RequestOptions()).load(post.images[2])
                        .into(img53)
                    glide.applyDefaultRequestOptions(RequestOptions()).load(post.images[3])
                        .into(img54)
                    glide.applyDefaultRequestOptions(RequestOptions()).load(post.images[4])
                        .into(img55)
                    tvImgNumber.text = "+${post.images.size - 5}"
                }
            }

            if (user.userId == post.userId) imvMenu.visibility = View.VISIBLE
            else imvMenu.visibility = View.INVISIBLE
        }

        fun getDate(milliSeconds: Long, dateFormat: String?): String? {
            val formatter = SimpleDateFormat(dateFormat, Locale.ENGLISH)
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = milliSeconds
            return formatter.format(calendar.time)
        }

        fun showImage(size: Int) {
            ctl1.visibility = View.GONE
            ctl2.visibility = View.GONE
            ctl3.visibility = View.GONE
            ctl4.visibility = View.GONE
            ctl5.visibility = View.GONE

            when (size) {
                1 -> {
                    ctl1.visibility = View.VISIBLE
                }
                2 -> {
                    ctl2.visibility = View.VISIBLE
                }
                3 -> {
                    ctl3.visibility = View.VISIBLE
                }
                4 -> {
                    ctl4.visibility = View.VISIBLE
                }
                5 -> {
                    ctl5.visibility = View.VISIBLE
                }
                else -> {
                }
            }
        }
    }

    class CreatePostViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private var imgAvatar: CircleImageView = view.findViewById(R.id.createpost_avatar)
        private var tvCreatePost: TextView = view.findViewById(R.id.createpost_textview)

        fun bind(glide: RequestManager, user: User, onClick: HomeOnClick) {
            glide.applyDefaultRequestOptions(RequestOptions()).load(user.photoUrl).into(imgAvatar)
            tvCreatePost.setOnClickListener {
                onClick.createPostClick()
            }
        }
    }
}