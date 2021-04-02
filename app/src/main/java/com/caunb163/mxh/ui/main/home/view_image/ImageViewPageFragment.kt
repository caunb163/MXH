package com.caunb163.mxh.ui.main.home.view_image

import android.view.View
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.caunb163.mxh.R
import com.caunb163.mxh.base.BaseFragment
import com.github.chrisbanes.photoview.PhotoView

class ImageViewPageFragment(
    private val position: Int,
    private val list: MutableList<String>,
    private val glide: RequestManager
) : BaseFragment(R.layout.fragment_image_view_page) {
    private lateinit var imageView: PhotoView

    override fun initView(view: View) {
        imageView = view.findViewById(R.id.imageview_view)
        glide.applyDefaultRequestOptions(RequestOptions()).load(list[position]).into(imageView)
    }

    override fun initListener() {}
    override fun initObserve() {}
}