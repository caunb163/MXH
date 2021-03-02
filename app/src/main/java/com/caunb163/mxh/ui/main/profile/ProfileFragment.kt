package com.caunb163.mxh.ui.main.profile

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.view.View
import android.widget.ProgressBar
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.caunb163.data.datalocal.LocalStorage
import com.caunb163.domain.model.PostEntity
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
    private lateinit var progressBar: ProgressBar

    private val viewModel: ProfileViewModel by inject()
    private val localStorage: LocalStorage by inject()
    private val list = mutableListOf<Any>()
    private lateinit var glide: RequestManager
    private lateinit var profileAdapter: ProfileAdapter
    private lateinit var user: User
    private lateinit var customProgressBar: CustomProgressBar

    private lateinit var uri: Uri

    override fun initView(view: View) {
        recyclerView = view.findViewById(R.id.profile_recyclerview)
        progressBar = view.findViewById(R.id.profile_progressbar)
        customProgressBar = CustomProgressBar(requireContext())

        glide = Glide.with(this)
        glide.applyDefaultRequestOptions(
            RequestOptions()
                .placeholder(R.drawable.image_default)
                .error(R.drawable.image_default)
        )

        user = localStorage.getAccount()!!
        profileAdapter = ProfileAdapter(glide, user, this, this)

        recyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = profileAdapter
        }
    }

    override fun initListener() {
    }

    override fun initObserve() {
        viewModel.getProfilePost()
        viewModel.statePost.observe(this, Observer { state ->
            when (state) {
                is State.Loading -> onLoading()
                is State.Success<*> -> onSuccess(state.data as MutableList<PostEntity>)
                is State.Failure -> onFailure(state.message)
            }
        })

        viewModel.stateAvatar.observe(this, Observer { state ->
            when (state) {
                is State.Loading -> onLoadingAvatar()
                is State.Success<*> -> onSuccess(state.data as String)
                is State.Failure -> onFailure(state.message)
            }
        })

        viewModel.stateBackground.observe(this, Observer { state ->
            when (state) {
                is State.Loading -> onLoadingAvatar()
                is State.Success<*> -> onSuccessBackground(state.data as String)
                is State.Failure -> onFailure(state.message)
            }
        })
    }

    private fun onLoading() {
        progressBar.visibility = View.VISIBLE
        recyclerView.visibility = View.INVISIBLE
    }

    private fun onLoadingAvatar() {
        customProgressBar.show()
    }

    private fun onSuccess(listPost: MutableList<PostEntity>) {
        progressBar.visibility = View.INVISIBLE
        recyclerView.visibility = View.VISIBLE
        list.clear()
        list.add(user)
        list.addAll(listPost)
        profileAdapter.updateData(list)
    }

    private fun onSuccess(uri: String) {
        customProgressBar.dismiss()
        for (obj in list) {
            if (obj is PostEntity) {
                obj.userAvatar = uri
            } else if (obj is User) {
                obj.photoUrl = uri
            }
        }
        profileAdapter.notifyDataSetChanged()
    }

    private fun onSuccessBackground(uri: String) {
        customProgressBar.dismiss()
        (list[0] as User).photoBackground = uri
        profileAdapter.notifyItemChanged(0)
    }

    private fun onFailure(message: String) {
        customProgressBar.dismiss()
        progressBar.visibility = View.INVISIBLE
        recyclerView.visibility = View.VISIBLE
        showToat(message)
    }

    override fun avatarClick() {
        ensurePermission(REQUEST_AVATAR_CODE)
    }

    override fun backgroundClick() {
        ensurePermission(REQUEST_BACKGROUND_CODE)
    }

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
            } else showToat("Yêu cầu bị từ chối")
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

    private fun uploadAvatar(uri: String) {
        viewModel.uploadAvatar(uri)
    }

    private fun uploadBackground(uri: String) {
        viewModel.uploadBackground(uri)
    }

    override fun createPostClick() {
    }

    override fun onCommentClick(postId: String) {
    }

}