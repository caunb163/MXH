package com.caunb163.mxh.ui.main.video

import android.view.View
import androidx.lifecycle.Observer
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.caunb163.domain.model.PostEntity
import com.caunb163.mxh.R
import com.caunb163.mxh.base.BaseFragment
import com.caunb163.mxh.state.State
import com.caunb163.mxh.ui.main.home.HomeOnClick
import kohii.v1.core.MemoryMode
import kohii.v1.exoplayer.Kohii
import org.koin.android.ext.android.inject

class VideoFragment : BaseFragment(R.layout.fragment_video), HomeOnClick {

    private val viewModel: VideoViewModel by inject()
    private val list = mutableListOf<PostEntity>()
    private lateinit var videoAdapter: VideoAdapter
    private lateinit var glide: RequestManager

    private lateinit var pager: ViewPager2
    private lateinit var kohii: Kohii

    override fun initView(view: View) {
        kohii = KohiiProvider.get(requireContext())
        pager = view.findViewById(R.id.video_viewPager2)
        kohii.register(this, memoryMode = MemoryMode.HIGH).addBucket(pager)

        glide = Glide.with(this)
        glide.applyDefaultRequestOptions(
            RequestOptions()
                .placeholder(R.drawable.image_default)
                .error(R.drawable.image_default)
        )

        videoAdapter = VideoAdapter(kohii, glide)
        pager.adapter = videoAdapter
        pager.offscreenPageLimit = 1
        videoAdapter.updateData(list)
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
    }

    private fun onLoading() {}

    private fun onSuccessListener(post: PostEntity) {
        if (post.userId.isEmpty()) {
            val index = checkPostIndex(post)
            if (index != -1) {
                list.removeAt(index)
                videoAdapter.notifyItemRemoved(index)
            }
        } else if (checkListAdd(post)) {
            val index = checkPostIndex(post)
            if (index != -1) {
                list[index] = post
                videoAdapter.notifyItemChanged(index)
            }
        } else {
            if (!list.contains(post)) {
                list.add(post)
                list.sortByDescending { it.createDate.inc() }
                videoAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun checkPostIndex(post: PostEntity): Int {
        for (index in 1 until list.size)
            if (list[index].postId == post.postId) {
                return index
            }
        return -1
    }

    private fun checkListAdd(post: PostEntity): Boolean {
        list.forEach {
            if (it.postId == post.postId) {
                return true
            }
        }
        return false
    }

    private fun onFailure(message: String) {
        showToast(message)
    }

    override fun createPostClick() {}

    override fun onCommentClick(post: PostEntity) {}

    override fun onLikeClick(postId: String) {}

    override fun onShareClick(content: String) {}

    override fun onEditClick(post: PostEntity) {}

    override fun onDeleteClick(post: PostEntity) {}

    override fun onImageClick(post: PostEntity, position: Int) {}
}