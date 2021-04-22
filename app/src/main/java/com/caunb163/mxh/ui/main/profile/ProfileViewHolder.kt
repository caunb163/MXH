package com.caunb163.mxh.ui.main.profile

import android.annotation.SuppressLint
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.caunb163.domain.model.User
import com.caunb163.mxh.R
import com.google.android.material.button.MaterialButton
import de.hdodenhof.circleimageview.CircleImageView

class ProfileViewHolder(view: View, private val requestManager: RequestManager) :
    RecyclerView.ViewHolder(view) {
    private var imvBackground: ImageView = view.findViewById(R.id.profile_background)
    private var imvAvatar: CircleImageView = view.findViewById(R.id.profile_avatar)
    private var username: TextView = view.findViewById(R.id.profile_user)
    private var intro: TextView = view.findViewById(R.id.profile_intro)
    private var gender: TextView = view.findViewById(R.id.profile_edt_gender)
    private var birthday: TextView = view.findViewById(R.id.profile_edt_birthday)
    private var phone: TextView = view.findViewById(R.id.profile_edt_phone)
    private var edit: MaterialButton = view.findViewById(R.id.profile_updateprofile)
    private val glide = requestManager

    private var imvCameraAvatar: CircleImageView = view.findViewById(R.id.profile_camera_avatar)
    private var imvCameraBackground: CircleImageView =
        view.findViewById(R.id.profile_camera_background)

    @SuppressLint("SetTextI18n")
    fun bind(user: User, profileOnClick: ProfileOnClick) {
        glide.applyDefaultRequestOptions(RequestOptions()).load(user.photoBackground)
            .into(imvBackground)
        glide.applyDefaultRequestOptions(RequestOptions()).load(user.photoUrl).into(imvAvatar)
        username.text = user.username
        intro.text = user.intro
        gender.text = "Giới tính: ${user.gender}"
        birthday.text = user.birthDay
        phone.text = user.phone

        edit.setOnClickListener { profileOnClick.editProfile() }
        imvCameraAvatar.setOnClickListener { profileOnClick.avatarClick() }
        imvCameraBackground.setOnClickListener { profileOnClick.backgroundClick() }
    }

    fun unbind() {
        imvBackground.setImageDrawable(null)
        imvAvatar.setImageDrawable(null)
        edit.setOnClickListener(null)
        imvCameraAvatar.setOnClickListener(null)
        imvCameraBackground.setOnClickListener(null)
        glide.clear(imvBackground)
        glide.clear(imvAvatar)
    }
}