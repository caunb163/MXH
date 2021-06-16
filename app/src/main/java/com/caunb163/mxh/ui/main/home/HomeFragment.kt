package com.caunb163.mxh.ui.main.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.caunb163.data.datalocal.LocalStorage
import com.caunb163.data.repository.RepositoryUser
import com.caunb163.domain.model.Post
import com.caunb163.domain.model.User
import com.caunb163.mxh.MainActivity
import com.caunb163.mxh.R
import com.caunb163.mxh.base.BaseFragment
import com.caunb163.mxh.state.State
import com.google.android.material.appbar.MaterialToolbar
import kotlinx.coroutines.InternalCoroutinesApi
import org.koin.android.ext.android.inject

@InternalCoroutinesApi
@Suppress("UNCHECKED_CAST")
class HomeFragment : BaseFragment(R.layout.fragment_home), HomeOnClick {
    private val TAG = "HomeFragment"

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private val viewModel: HomeViewModel by inject()
    private val localStorage: LocalStorage by inject()
    private val repositoryUser: RepositoryUser by inject()
    private val list = mutableListOf<Any>()
    private lateinit var glide: RequestManager
    private lateinit var homeAdapter: HomeAdapter
    private lateinit var user: User
    private lateinit var toolbar: MaterialToolbar
    private val listAds = mutableListOf<Post>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    @SuppressLint("ResourceType")
    override fun initView(view: View) {
        recyclerView = view.findViewById(R.id.home_recycleview)
        progressBar = view.findViewById(R.id.home_progressbar)
        toolbar = view.findViewById(R.id.home_toolbar)

        toolbar.inflateMenu(R.menu.menu_option)
        toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.logout -> {
                    viewModel.logout()
                    (activity as MainActivity).findNavController(R.id.main_nav_host_fragment)
                        .navigate(R.id.action_mainFragment_to_splashFragment)
                }
            }
            true
        }

        glide = Glide.with(this)
        glide.applyDefaultRequestOptions(
            RequestOptions()
                .placeholder(R.drawable.image_default)
                .error(R.drawable.image_default)
        )
        user = localStorage.getAccount()!!
        homeAdapter = HomeAdapter(glide, this, user, repositoryUser)

        recyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = homeAdapter
        }
        list.add(user)
        homeAdapter.updateData(list)
    }

    override fun initListener() {}

    override fun initObserve() {
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

        viewModel.stateDelete.observe(this, Observer { state ->
            when (state) {
                is State.Loading -> onLoading()
                is State.Success<*> -> onSuccessLike()
                is State.Failure -> onFailure(state.message)
            }
        })

        viewModel.listenerAds.observe(this, Observer { state ->
            when (state) {
                is State.Loading -> onLoading()
                is State.Success<*> -> onSuccessAds(state.data as Post)
                is State.Failure -> onFailure(state.message)
            }
        })
    }

    private fun onLoading() {}

    private fun onSuccessLike() {}

    private fun onSuccessAds(postEntity: Post) { listAds.add(postEntity) }

    private fun onSuccessListener(post: Post) {
        if (post.userId.isEmpty()) {
            val index = checkPostIndex(post)
            if (index != -1) {
                list.removeAt(index)
                homeAdapter.notifyItemRemoved(index)
            }
        } else if (checkListAdd(post)) {
            val index = checkPostIndex(post)
            if (index != -1) {
                if (post.active) {
                    list[index] = post
                    homeAdapter.notifyItemChanged(index)
                } else {
                    list.removeAt(index)
                    homeAdapter.notifyItemRemoved(index)
                }
            }
        } else {
            if (!list.contains(post)) {
                if (post.active) {
                    if (list.size % 8 == 0) {
                        val index = (0 until listAds.size).random()
                        Log.e(TAG, "random: $index")
                        list.add(listAds[index])
                        homeAdapter.notifyItemInserted(list.size)
                    }
                    if (checkCreatePost(post)) {
                        list.add(1, post)
                        homeAdapter.notifyItemInserted(1)
                    } else {
                        list.add(post)
                        homeAdapter.notifyItemChanged(list.size)
                    }
                }
            }
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

    private fun checkCreatePost(post: Post): Boolean {
        list.forEach {
            if (it is Post) {
                if (post.createDate > it.createDate) {
                    return true
                }
            }
        }
        return false
    }

    private fun onFailure(message: String) { showToast(message) }

    override fun createPostClick() { findNavController().navigate(R.id.action_homeFragment_to_createPostFragment) }

    override fun onCommentClick(post: Post) {
        val action = HomeFragmentDirections.actionHomeFragmentToCommentFragment(post)
        findNavController().navigate(action)
    }

    override fun onLikeClick(postId: String) { viewModel.likePost(postId) }

    override fun onShareClick(content: String) {
        val sharingIntent = Intent(Intent.ACTION_SEND)
        sharingIntent.type = "text/plain"
        sharingIntent.putExtra(Intent.EXTRA_TEXT, content)
        startActivity(Intent.createChooser(sharingIntent, "Chia sẻ nội dung"))
    }

    override fun onEditClick(post: Post) {
        val action = HomeFragmentDirections.actionHomeFragmentToEditPostFragment(post)
        findNavController().navigate(action)
    }

    override fun onDeleteClick(post: Post) {
        val alertDialog = AlertDialog.Builder(requireContext())
        alertDialog.setTitle("Xóa bài viết")
        alertDialog.setMessage("Bạn có chắc muốn xóa bài viết này không?")
        alertDialog.setPositiveButton("Có") { _, _ ->
            viewModel.deletePost(post)
        }
        alertDialog.setNegativeButton("Không") { dialog, _ -> dialog.dismiss() }
        alertDialog.show()
    }

    override fun onImageClick(post: Post, position: Int) {
        val action = HomeFragmentDirections.actionHomeFragmentToListImageFragment(post, position)
        findNavController().navigate(action)
    }

    override fun onAvatarClick(userId: String) {
        if (TextUtils.equals(userId, user.userId)) {
            findNavController().navigate(R.id.action_homeFragment_to_profileFragment)
        } else {
            val action = HomeFragmentDirections.actionHomeFragmentToUserFragment(userId)
            findNavController().navigate(action)
        }
    }
}