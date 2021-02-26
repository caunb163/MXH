package com.caunb163.mxh.ui.main.profile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.caunb163.domain.model.PostEntity
import com.caunb163.domain.model.User
import com.caunb163.mxh.R
import com.caunb163.mxh.ui.main.home.PostViewHolder
import de.hdodenhof.circleimageview.CircleImageView

class ProfileAdapter(
    private val glide: RequestManager,
    private val user: User,
    private val onClick: ProfileOnClick
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TYPE_PROFILE = 0
    private val TYPE_POST = 1

    private var list: MutableList<Any> = mutableListOf()
    private var userLocal = user

    fun updateData(datas: MutableList<Any>) {
        list = datas
        notifyDataSetChanged()
    }

    fun updateUser(userChange: User) {
        userLocal = userChange
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_PROFILE) {
            ProfileViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.layout_profile, parent, false)
            )
        } else PostViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.layout_post_default, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.itemViewType == TYPE_POST) {
            (holder as PostViewHolder).bind(glide, list[position] as PostEntity, userLocal)
        } else {
            (holder as ProfileViewHolder).bind(glide, list[position] as User, onClick)
        }
    }

    override fun getItemCount(): Int = list.size

    override fun getItemViewType(position: Int): Int {
        return if (list[position] is User) {
            TYPE_PROFILE
        } else TYPE_POST
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        super.onViewRecycled(holder)
        if (holder is ProfileViewHolder) holder.unbind()
        else if (holder is PostViewHolder) holder.unbind()
    }

    class ProfileViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private var imvBackground: ImageView = view.findViewById(R.id.profile_background)
        private var imvAvatar: CircleImageView = view.findViewById(R.id.profile_avatar)
        private var username: TextView = view.findViewById(R.id.profile_user)
        private var intro: TextView = view.findViewById(R.id.profile_intro)
        private var address: EditText = view.findViewById(R.id.profile_edt_address)
        private var birthday: EditText = view.findViewById(R.id.profile_edt_birthday)
        private var phone: EditText = view.findViewById(R.id.profile_edt_phone)
        private var imvAdress: ImageView = view.findViewById(R.id.profile_imv_map)
        private var imvBirthday: ImageView = view.findViewById(R.id.profile_imv_birthday)
        private var imvPhone: ImageView = view.findViewById(R.id.profile_imv_phone)
        private var imvFixAddress: ImageView = view.findViewById(R.id.profile_fix_address)
        private var imvFixBirthday: ImageView = view.findViewById(R.id.profile_fix_birthday)
        private var imvFixPhone: ImageView = view.findViewById(R.id.profile_fix_phone)

        private var imvCameraAvatar: CircleImageView = view.findViewById(R.id.profile_camera_avatar)
        private var imvCameraBackground: CircleImageView =
            view.findViewById(R.id.profile_camera_background)

        fun bind(glide: RequestManager, user: User, onClick: ProfileOnClick) {
            glide.applyDefaultRequestOptions(RequestOptions()).load(user.photoBackground)
                .into(imvBackground)
            glide.applyDefaultRequestOptions(RequestOptions()).load(user.photoUrl).into(imvAvatar)
            username.text = user.username
            intro.text = user.intro
            address.setText(user.address)
            birthday.setText(user.birthDay)
            phone.setText(user.phone)
            addListener(onClick)
        }

        fun unbind() {
            imvBackground.setImageDrawable(null)
            imvAvatar.setImageDrawable(null)
        }

        private fun addListener(onClick: ProfileOnClick) {
            imvCameraAvatar.setOnClickListener { onClick.avatarClick() }

            imvCameraBackground.setOnClickListener { onClick.backgroundClick() }
        }
    }
}