package com.caunb163.mxh.ui.main.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
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
import com.caunb163.domain.model.PostEntity
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
    private val list = mutableListOf<Any>()
    private lateinit var glide: RequestManager
    private lateinit var homeAdapter: HomeAdapter
    private lateinit var user: User
    private lateinit var toolbar: MaterialToolbar

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
        homeAdapter = HomeAdapter(glide, this, user)

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
                is State.Success<*> -> onSuccessListener(state.data as PostEntity)
                is State.Failure -> onFailure(state.message)
            }
        })

        viewModel.stateLike.observe(this, Observer { state ->
            when (state) {
                is State.Loading -> onLoadingLike()
                is State.Success<*> -> onSuccessLike()
                is State.Failure -> onFailure(state.message)
            }
        })

        viewModel.stateDelete.observe(this, Observer { state ->
            when (state) {
                is State.Loading -> onLoadingLike()
                is State.Success<*> -> onSuccessLike()
                is State.Failure -> onFailure(state.message)
            }
        })
    }

    private fun onLoading() {
//        progressBar.visibility = View.VISIBLE
//        recyclerView.visibility = View.INVISIBLE
    }

    private fun onLoadingLike() {}

    private fun onSuccessLike() {}

    private fun onSuccessListener(post: PostEntity) {
        if (post.userId.isEmpty()) {
            val index = checkPostIndex(post)
            if (index != -1) {
                list.removeAt(index)
                homeAdapter.notifyItemRemoved(index)
            }
        } else if (checkListAdd(post)) {
            val index = checkPostIndex(post)
            if (index != -1) {
                list[index] = post
                homeAdapter.notifyItemChanged(index)
            }
        } else {
            if (!list.contains(post)) {
                list.add(post)
                list.sortByDescending {
                    if (it is PostEntity) {
                        it.createDate.inc()
                    } else {
                        null
                    }
                }
                list.remove(user)
                list.add(0,user)
                homeAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun checkPostIndex(post: PostEntity): Int {
        for (index in 1 until list.size)
            if ((list[index] as PostEntity).postId == post.postId) {
                return index
            }
        return -1
    }

    private fun checkListAdd(post: PostEntity): Boolean {
        list.forEach {
            if (it is PostEntity) {
                if (it.postId == post.postId) {
                    return true
                }
            }
        }
        return false
    }

    private fun onFailure(message: String) {
        progressBar.visibility = View.INVISIBLE
        recyclerView.visibility = View.VISIBLE
        showToast(message)
    }

    override fun createPostClick() {
        findNavController().navigate(R.id.action_homeFragment_to_createPostFragment)
    }

    override fun onCommentClick(post: PostEntity) {
        val action = HomeFragmentDirections.actionHomeFragmentToCommentFragment(post)
        findNavController().navigate(action)
    }

    override fun onLikeClick(postId: String) {
        viewModel.likePost(postId)
    }

    override fun onShareClick(content: String) {
        val sharingIntent = Intent(Intent.ACTION_SEND)
        sharingIntent.type = "text/plain"
        sharingIntent.putExtra(Intent.EXTRA_TEXT, content)
        startActivity(Intent.createChooser(sharingIntent, "Chia sẻ nội dung"))
    }

    override fun onEditClick(post: PostEntity) {
        val action = HomeFragmentDirections.actionHomeFragmentToEditPostFragment(post)
        findNavController().navigate(action)
    }

    override fun onDeleteClick(post: PostEntity) {
        val alertDialog = AlertDialog.Builder(requireContext())
        alertDialog.setTitle("Xóa bài viết")
        alertDialog.setMessage("Bạn có chắc muốn xóa bài viết này không?")
        alertDialog.setPositiveButton("Có") { _, _ ->
            viewModel.deletePost(post)
        }
        alertDialog.setNegativeButton("Không") { dialog, _ -> dialog.dismiss() }
        alertDialog.show()
    }

    override fun onImageClick(post: PostEntity, position: Int) {
        val action = HomeFragmentDirections.actionHomeFragmentToListImageFragment(post, position)
        findNavController().navigate(action)
    }
}