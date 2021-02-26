package com.caunb163.mxh.ui.main.profile

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.view.View
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
import org.koin.android.ext.android.inject

@Suppress("UNCHECKED_CAST")
class ProfileFragment : BaseFragment(R.layout.fragment_profile), ProfileOnClick {
    private val TAG = "ProfileFragment"
    private val REQUEST_AVATAR_CODE = 222
    private val REQUEST_BACKGROUND_CODE = 333

    private lateinit var recyclerView: RecyclerView

    private val viewModel: ProfileViewModel by inject()
    private val localStorage: LocalStorage by inject()
    private val list = mutableListOf<Any>()
    private lateinit var glide: RequestManager
    private lateinit var profileAdapter: ProfileAdapter
    private lateinit var user: User

    private var listPermission = mutableListOf<String>(Manifest.permission.READ_EXTERNAL_STORAGE)
    private lateinit var uri: Uri

    override fun initView(view: View) {
        recyclerView = view.findViewById(R.id.profile_recyclerview)

        glide = Glide.with(this)
        glide.applyDefaultRequestOptions(
            RequestOptions()
                .placeholder(R.drawable.image_default)
                .error(R.drawable.image_default)
        )

        user = localStorage.getAccount()!!
        profileAdapter = ProfileAdapter(glide, user, this)

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
                is State.Loading -> onLoading()
                is State.Success<*> -> onSuccess()
                is State.Failure -> onFailure(state.message)
            }
        })
    }

    private fun onLoading() {}

    private fun onSuccess(listPost: MutableList<PostEntity>) {
        list.clear()
        list.add(user)
        list.addAll(listPost)
        profileAdapter.updateData(list)
    }

    private fun onSuccess() {
        showToat("Success")
    }

    private fun onFailure(message: String) {
        showToat(message)
    }

    override fun avatarClick() {
        ensurePermission(REQUEST_AVATAR_CODE)
    }

    override fun backgroundClick() {
        ensurePermission(REQUEST_BACKGROUND_CODE)
    }

    private fun ensurePermission(requestCode: Int) {
        if (checkPermission(listPermission)) {
            openLibrary(requestCode)
        } else
            requestPermissions(listPermission.toTypedArray(), requestCode)
    }

    private fun checkPermission(listPermission: MutableList<String>): Boolean {
        if (listPermission.isNullOrEmpty()) {
            for (permission in listPermission) {
                if (ActivityCompat.checkSelfPermission(
                        requireContext(),
                        permission
                    ) != PackageManager.PERMISSION_GRANTED
                )
                    return false
            }
        }

        return true
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
            if (checkPermission(listPermission)) {
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
}