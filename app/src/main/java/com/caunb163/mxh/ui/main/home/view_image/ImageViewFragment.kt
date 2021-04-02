package com.caunb163.mxh.ui.main.home.view_image

import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.navigation.fragment.navArgs
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.caunb163.mxh.R
import com.caunb163.mxh.base.BaseDialogFragment

class ImageViewFragment : BaseDialogFragment() {
    private lateinit var viewPager: ViewPager
    private lateinit var glide: RequestManager
    private val args: ImageViewFragmentArgs by navArgs()

    override fun getLayoutId(): Int = R.layout.fragment_image_view

    override fun initView(view: View) {
        viewPager = view.findViewById(R.id.imageview_viewpager)

        glide = Glide.with(this)
        glide.applyDefaultRequestOptions(
            RequestOptions()
                .placeholder(R.drawable.image_default)
                .error(R.drawable.image_default)
        )

        viewPager.adapter = ImageViewPageAdapter(
            childFragmentManager,
            FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,
            args.images.toMutableList(),
            glide
        )
        viewPager.setCurrentItem(args.position, false)
    }

    override fun initListener() {

    }

    override fun initObserve() {
    }

}

class ImageViewPageAdapter(
    fm: FragmentManager,
    behavior: Int,
    private val images: MutableList<String>,
    private val glide: RequestManager
) :
    FragmentStatePagerAdapter(fm, behavior) {

    override fun getCount(): Int = images.size

    override fun getItem(position: Int): Fragment = ImageViewPageFragment(position, images, glide)

}
