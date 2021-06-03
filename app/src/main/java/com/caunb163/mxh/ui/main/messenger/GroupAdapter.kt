package com.caunb163.mxh.ui.main.messenger

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.ViewFlipper
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.caunb163.data.repository.RepositoryUser
import com.caunb163.domain.model.Group
import com.caunb163.domain.model.User
import com.caunb163.mxh.R
import com.github.satoshun.coroutine.autodispose.view.autoDisposeScope
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.launch

class GroupAdapter(
    private val glide: RequestManager,
    private val user: User,
    private val listener: OnGroupClickListener,
    private val repositoryUser: RepositoryUser
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var list: MutableList<Group> = mutableListOf()

    fun updateData(datas: MutableList<Group>) {
        list = datas
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return GroupViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.layout_group, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as GroupViewHolder).bind(glide, list[position], user, listener, repositoryUser)
    }

    override fun getItemCount(): Int = list.size

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        super.onViewRecycled(holder)
        (holder as GroupViewHolder).unbind()
    }

    class GroupViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val TAG = "GroupAdapter"

        private var civAvatar1: CircleImageView = view.findViewById(R.id.group_imv1)
        private var civAvatar2: CircleImageView = view.findViewById(R.id.group_imv2)
        private var civAvatar3: CircleImageView = view.findViewById(R.id.group_imv3)
        private var viewFlipper: ViewFlipper = view.findViewById(R.id.group_viewFlipper)
        private var groupName: TextView = view.findViewById(R.id.group_name)
        private var groupLastMessage: TextView = view.findViewById(R.id.group_lastMessage)
        private var groupview: ConstraintLayout = view.findViewById(R.id.group_view)

        fun bind(
            glide: RequestManager,
            group: Group,
            user: User,
            listener: OnGroupClickListener,
            repositoryUser: RepositoryUser
        ) {
            itemView.autoDisposeScope.launch {
                val user1 = repositoryUser.getUser(group.arrUserId[0])
                val user2 = repositoryUser.getUser(group.arrUserId[1])
                if (group.arrUserId.size == 2) {
                    viewFlipper.displayedChild = 1
                    if (TextUtils.equals(user1.userId, user.userId)) {
                        glide.applyDefaultRequestOptions(RequestOptions())
                            .load(user2.photoUrl).into(civAvatar3)
                    } else glide.applyDefaultRequestOptions(RequestOptions())
                        .load(user1.photoUrl).into(civAvatar3)
                } else {
                    viewFlipper.displayedChild = 0
                    glide.applyDefaultRequestOptions(RequestOptions())
                        .load(user1.photoUrl).into(civAvatar1)
                    glide.applyDefaultRequestOptions(RequestOptions())
                        .load(user2.photoUrl).into(civAvatar2)
                }
            }

            groupName.text = group.name
            groupLastMessage.text = group.lastMessage
            groupview.setOnClickListener { listener.onGroupClick(group) }
        }

        fun unbind() {
            civAvatar1.setImageDrawable(null)
            civAvatar2.setImageDrawable(null)
            civAvatar3.setImageDrawable(null)
            groupview.setOnClickListener(null)
        }
    }
}

interface OnGroupClickListener {
    fun onGroupClick(group: Group)
}