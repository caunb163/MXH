package com.caunb163.mxh.ui.main.profile

import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
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
import com.caunb163.mxh.R
import com.caunb163.mxh.base.BaseFragment
import com.caunb163.mxh.state.State
import com.caunb163.mxh.ui.main.home.HomeOnClick
import com.caunb163.mxh.ultis.CheckPermission
import com.caunb163.mxh.ultis.CustomProgressBar
import org.koin.android.ext.android.inject

@Suppress("UNCHECKED_CAST")
class ProfileFragment : BaseFragment(R.layout.fragment_profile), ProfileOnClick, HomeOnClick {
    private val TAG = "ProfileFragment"
    private val REQUEST_AVATAR_CODE = 222
    private val REQUEST_BACKGROUND_CODE = 333

    private lateinit var recyclerView: RecyclerView

    private val viewModel: ProfileViewModel by inject()
    private val localStorage: LocalStorage by inject()
    private val repositoryUser: RepositoryUser by inject()
    private val list = mutableListOf<Any>()
    private lateinit var glide: RequestManager
    private lateinit var profileAdapter: ProfileAdapter
    private lateinit var user: User
    private lateinit var customProgressBar: CustomProgressBar

    private lateinit var uri: Uri

    override fun initView(view: View) {
        recyclerView = view.findViewById(R.id.profile_recyclerview)
        customProgressBar = CustomProgressBar(requireContext())

        glide = Glide.with(this)
        glide.applyDefaultRequestOptions(
            RequestOptions()
                .placeholder(R.drawable.image_default)
                .error(R.drawable.image_default)
        )

        user = localStorage.getAccount()!!
        profileAdapter = ProfileAdapter(glide, user, this, this, repositoryUser)

        recyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = profileAdapter
        }
        list.add(user)
        profileAdapter.updateData(list)
    }

    override fun initListener() {}

    override fun initObserve() {
        viewModel.setUserId(user.userId)
        viewModel.listener.observe(this, Observer { state ->
            when (state) {
                is State.Loading -> onLoading()
                is State.Success<*> -> onSuccessListener(state.data as Post)
                is State.Failure -> onFailure(state.message)
            }
        })

        viewModel.stateAvatar.observe(this, Observer { state ->
            when (state) {
                is State.Loading -> onLoadingAvatar()
                is State.Success<*> -> onSuccessAvatar()
                is State.Failure -> onFailure(state.message)
            }
        })

        viewModel.stateBackground.observe(this, Observer { state ->
            when (state) {
                is State.Loading -> onLoadingAvatar()
                is State.Success<*> -> onSuccessAvatar()
                is State.Failure -> onFailure(state.message)
            }
        })

        viewModel.user.observe(this, Observer { state ->
            when (state) {
                is State.Loading -> onLoading()
                is State.Success<*> -> onSuccessUser(state.data as User)
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
    }

    private fun onLoading() {}

    private fun onLoadingAvatar() { customProgressBar.show() }

    private fun onSuccessUser(u: User) {
        list.remove(user)
        list.add(0, u)
        user = u
        profileAdapter.notifyDataSetChanged()
    }

    private fun onSuccessLike() {}

    private fun onSuccessListener(post: Post) {
        if (post.userId.isEmpty()) {
            val index = checkPostIndex(post)
            if (index != -1) {
                list.removeAt(index)
                profileAdapter.notifyItemRemoved(index)
            }
        } else if (checkListAdd(post)) {
            val index = checkPostIndex(post)
            if (index != -1) {
                list[index] = post
                profileAdapter.notifyItemChanged(index)
            }
        } else {
            list.add(post)
            profileAdapter.notifyItemInserted(list.size - 1)
        }
    }

    private fun onSuccessAvatar() {
        customProgressBar.dismiss()
        profileAdapter.notifyDataSetChanged()
    }

    private fun onFailure(message: String) {
        customProgressBar.dismiss()
        showToast(message)
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

    override fun avatarClick() { ensurePermission(REQUEST_AVATAR_CODE) }

    override fun backgroundClick() { ensurePermission(REQUEST_BACKGROUND_CODE) }

    override fun editProfile() { findNavController().navigate(R.id.action_profileFragment_to_updateProfileFragment) }

    private fun ensurePermission(requestCode: Int) {
        if (CheckPermission.checkPermission(requireContext())) {
            openLibrary(requestCode)
        } else
            requestPermissions(CheckPermission.listPermission.toTypedArray(), requestCode)
    }

    private fun openLibrary(requestCode: Int) {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_PICK
        startActivityForResult(intent, requestCode)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if ((requestCode == REQUEST_AVATAR_CODE) || (requestCode == REQUEST_BACKGROUND_CODE)) {
            if (CheckPermission.checkPermission(requireContext())) {
                openLibrary(requestCode)
            } else showToast("Yêu cầu bị từ chối")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_AVATAR_CODE -> {
                data?.let {
                    uri = data.data!!
                    uploadAvatar(uri.toString())
                }
            }

            REQUEST_BACKGROUND_CODE -> {
                data?.let {
                    uri = data.data!!
                    uploadBackground(uri.toString())
                }
            }
        }
    }

    private fun uploadAvatar(uri: String) { viewModel.uploadAvatar(uri) }

    private fun uploadBackground(uri: String) { viewModel.uploadBackground(uri) }

    override fun createPostClick() {}

    override fun onCommentClick(post: Post) {
        val action = ProfileFragmentDirections.actionProfileFragmentToCommentFragment(post)
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
        val action = ProfileFragmentDirections.actionProfileFragmentToEditPostFragment(post)
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
        val action = ProfileFragmentDirections.actionProfileFragmentToListImageFragment(post, position)
        findNavController().navigate(action)
    }

    override fun onAvatarClick(userId: String) {}

}