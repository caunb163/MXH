package com.caunb163.mxh.ui.main.home

import android.annotation.SuppressLint
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.caunb163.domain.model.PostEntity
import com.caunb163.domain.model.User
import com.caunb163.mxh.R
import de.hdodenhof.circleimageview.CircleImageView
import java.text.SimpleDateFormat
import java.util.*

class PostViewHolder(view: View) : RecyclerView.ViewHolder(view) {
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
    fun bind(glide: RequestManager, post: PostEntity, user: User, onClick: HomeOnClick) {

        username.text = post.userName
        glide.applyDefaultRequestOptions(RequestOptions()).load(post.userAvatar).into(imgAvatar)

        createDate.text = getDate(post.createDate)
        content.text = post.content
        likeNumber.text = post.arrLike.size.toString()
        commentNumber.text = "${post.arrCmtId.size} bình luận"

        comment.setOnClickListener {
            onClick.onCommentClick(post.postId)
        }


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

                if (post.images.size == 5) {
                    tvImgNumber.visibility = View.INVISIBLE
                } else {
                    tvImgNumber.visibility = View.VISIBLE
                    tvImgNumber.text = "+${post.images.size - 5}"
                }
            }
        }

        if (user.userId == post.userId) imvMenu.visibility = View.VISIBLE
        else imvMenu.visibility = View.INVISIBLE
    }

    fun unbind() {
        imgAvatar.setImageDrawable(null)
        comment.setOnClickListener(null)
    }

    private fun getDate(milliSeconds: Long): String? {
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = milliSeconds
        return formatter.format(calendar.time)
    }

    private fun showImage(size: Int) {
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