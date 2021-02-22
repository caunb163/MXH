package com.caunb163.mxh.ui.main.home

import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.caunb163.data.datalocal.LocalStorage
import com.caunb163.domain.model.Post
import com.caunb163.mxh.R
import com.caunb163.mxh.base.BaseFragment
import com.caunb163.mxh.state.State
import org.koin.android.ext.android.inject

@Suppress("UNCHECKED_CAST")
class HomeFragment : BaseFragment(R.layout.fragment_home) {
    private val TAG = "HomeFragment"

    private lateinit var recyclerView: RecyclerView
    private val viewModel: HomeViewModel by inject()
    private val localStorage: LocalStorage by inject()
    private val list = mutableListOf<Any>()
    private lateinit var glide: RequestManager
    private lateinit var homeAdapter: HomeAdapter

    override fun initView(view: View) {
        recyclerView = view.findViewById(R.id.home_recycleview)

        glide = Glide.with(this)
        glide.applyDefaultRequestOptions(
            RequestOptions()
                .placeholder(R.drawable.bg1)
                .error(R.drawable.bg1)
        )

        homeAdapter = HomeAdapter(glide)

        recyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = homeAdapter
        }
    }

    override fun initListener() {
    }

    override fun initObserve() {
        viewModel.getAllPost()
        viewModel.state.observe(this, Observer { state ->
            when (state) {
                is State.Loading -> onLoading()
                is State.Success<*> -> onSuccess(state.data as MutableList<Post>)
                is State.Failure -> onFailure(state.message)
            }
        })
    }

    private fun onLoading() {}

    private fun onSuccess(listPost: MutableList<Post>) {
        Log.e(TAG, "onSuccess: $listPost")
        localStorage.getAccount()?.let { list.add(it) }
        list.addAll(listPost)
        homeAdapter.updateData(list)
    }

    private fun onFailure(message: String) {
        Log.e(TAG, "onFailure: $message")
    }
}