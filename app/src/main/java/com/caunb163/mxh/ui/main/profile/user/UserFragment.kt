package com.caunb163.mxh.ui.main.profile.user

import android.content.Intent
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.NavArgs
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.caunb163.data.datalocal.LocalStorage
import com.caunb163.data.repository.RepositoryUser
import com.caunb163.domain.model.Post
import com.caunb163.domain.model.User
import com.caunb163.mxh.R
import com.caunb163.mxh.base.BaseDialogFragment
import com.caunb163.mxh.state.State
import com.caunb163.mxh.ui.main.home.HomeOnClick
import com.caunb163.mxh.ui.main.profile.ProfileAdapter
import com.caunb163.mxh.ui.main.profile.ProfileFragmentDirections
import com.caunb163.mxh.ui.main.profile.ProfileOnClick
import com.caunb163.mxh.ui.main.profile.ProfileViewModel
import com.caunb163.mxh.ultis.CustomProgressBar
import kotlinx.coroutines.*
import org.koin.android.ext.android.inject

class UserFragment : BaseDialogFragment(), HomeOnClick {

    private lateinit var recyclerView: RecyclerView
    private val viewModel: UserViewModel by inject()
    private val repositoryUser: RepositoryUser by inject()
    private val args: UserFragmentArgs by navArgs()
    private val localStorage: LocalStorage by inject()
    private val list = mutableListOf<Any>()
    private lateinit var glide: RequestManager
    private lateinit var userAdapter: UserAdapter
    private lateinit var user: User

    override fun getLayoutId(): Int = R.layout.fragment_user

    override fun initView(view: View) {
        recyclerView = view.findViewById(R.id.user_recyclerview)

        glide = Glide.with(this)
        glide.applyDefaultRequestOptions(
            RequestOptions()
                .placeholder(R.drawable.image_default)
                .error(R.drawable.image_default)
        )

        user = localStorage.getAccount()!!
        userAdapter = UserAdapter(glide, user, this, repositoryUser)

        recyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = userAdapter
        }
        list.add(args.userId)
        userAdapter.updateData(list)
    }

    override fun initListener() {}

    override fun initObserve() {
        viewModel.setUserId(args.userId)
        viewModel.listener.observe(this, Observer { state ->
            when (state) {
                is State.Loading -> onLoading()
                is State.Success<*> -> onSuccessListener(state.data as Post)
                is State.Failure -> onFailure(state.message)
            }
        })

        viewModel.stateLike.observe(this, Observer { state ->
            when (state) {
                is State.Loading -> onLoading()
                is State.Success<*> -> onSuccessLike()
                is State.Failure -> onFailure(state.message)
            }
        })
    }

    private fun onLoading() {}

    private fun onSuccessLike() {}

    private fun onSuccessListener(post: Post) {
        if (post.userId.isEmpty()) {
            val index = checkPostIndex(post)
            if (index != -1) {
                list.removeAt(index)
                userAdapter.notifyItemRemoved(index)
            }
        } else if (checkListAdd(post)) {
            val index = checkPostIndex(post)
            if (index != -1) {
                list[index] = post
                userAdapter.notifyItemChanged(index)
            }
        } else {
            list.add(post)
            userAdapter.notifyItemInserted(list.size - 1)
        }
    }

    private fun checkPostIndex(post: Post): Int {
        for (index in 1 until list.size)
            if ((list[index] as Post).postId == post.postId) {
                return index
            }
        return -1
    }

    private fun checkListAdd(post: Post): Boolean {
        list.forEach {
            if (it is Post) {
                if (it.postId == post.postId) {
                    return true
                }
            }
        }
        return false
    }

    private fun onFailure(message: String) { showToast(message) }

    override fun createPostClick() {}

    override fun onCommentClick(post: Post) {
        val action = UserFragmentDirections.actionUserFragmentToCommentFragment(post)
        findNavController().navigate(action)
    }

    override fun onLikeClick(postId: String) { viewModel.likePost(postId) }

    override fun onShareClick(content: String) {
        val sharingIntent = Intent(Intent.ACTION_SEND)
        sharingIntent.type = "text/plain"
        sharingIntent.putExtra(Intent.EXTRA_TEXT, content)
        startActivity(Intent.createChooser(sharingIntent, "Chia sẻ nội dung"))
    }

    override fun onEditClick(post: Post) {}

    override fun onDeleteClick(post: Post) {}

    override fun onImageClick(post: Post, position: Int) {
        val action = UserFragmentDirections.actionUserFragmentToListImageFragment(post, position)
        findNavController().navigate(action)
    }

    override fun onAvatarClick(userId: String) {}
}