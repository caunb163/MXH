package com.caunb163.mxh.ui.main.messenger.chat.emoji

import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.navigation.fragment.navArgs
import androidx.viewpager.widget.ViewPager
import com.caunb163.domain.model.GroupEntity
import com.caunb163.mxh.R
import com.caunb163.mxh.base.BaseDialogFragment
import com.google.android.material.tabs.TabLayout

class EmojiFragment : BaseDialogFragment() {
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager
    private lateinit var emojiPagerAdapter: EmojiPagerAdapter
    private val args: EmojiFragmentArgs by navArgs()

    override fun getLayoutId(): Int = R.layout.fragment_emoji

    override fun initView(view: View) {
        tabLayout = view.findViewById(R.id.emoji_tablayout)
        viewPager = view.findViewById(R.id.emoji_viewpager)

        emojiPagerAdapter = EmojiPagerAdapter(
            childFragmentManager,
            FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,
            args.group
        )
        viewPager.adapter = emojiPagerAdapter
        tabLayout.setupWithViewPager(viewPager)
        tabLayout.getTabAt(0)?.text = "QooBee"
        tabLayout.getTabAt(1)?.text = "Chummy"
    }

    override fun initListener() {}

    override fun initObserve() {}
}

class EmojiPagerAdapter(
    fm: FragmentManager, behavior: Int,
    private val groupEntity: GroupEntity
) : FragmentPagerAdapter(fm, behavior) {

    override fun getCount(): Int = 2

    override fun getItem(position: Int): Fragment = EmojiPageFragment(position, groupEntity)
}