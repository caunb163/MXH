package com.caunb163.mxh.ui.main.messenger.create_group

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.caunb163.domain.model.User
import com.caunb163.mxh.R
import de.hdodenhof.circleimageview.CircleImageView

class UserAdapter(
    private val glide: RequestManager,
    private val listener: UserOnClick
) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    private var list: MutableList<User> = mutableListOf()

    fun updateData(datas: MutableList<User>) {
        list = datas
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.layout_user, parent, false)
        )
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(list[position], glide, listener)
    }

    override fun getItemCount(): Int = list.size

    override fun onViewRecycled(holder: UserViewHolder) {
        super.onViewRecycled(holder)
        holder.unbind()
    }

    class UserViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val userAvatar: CircleImageView = view.findViewById(R.id.user_avatar)
        private val userName: TextView = view.findViewById(R.id.user_name)
        private val checkBox: CheckBox = view.findViewById(R.id.user_checkbox)

        fun bind(user: User, glide: RequestManager, listener: UserOnClick) {
            glide.applyDefaultRequestOptions(RequestOptions()).load(user.photoUrl)
                .into(userAvatar)
            userName.text = user.username

            checkBox.isChecked = user.ischeck
            checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
                user.ischeck = isChecked
                listener.userOnClick(user)
            }
        }

        fun unbind() {
            userAvatar.setImageDrawable(null)
        }
    }
}

interface UserOnClick {
    fun userOnClick(u: User)
}