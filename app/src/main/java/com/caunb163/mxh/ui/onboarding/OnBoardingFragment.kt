package com.caunb163.mxh.ui.onboarding

import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import com.caunb163.domain.state.State
import com.caunb163.mxh.R
import com.caunb163.mxh.base.BaseFragment
import com.google.android.material.tabs.TabLayout
import org.koin.android.ext.android.inject

class OnBoardingFragment : BaseFragment(R.layout.fragment_on_boarding),
    ViewPager.OnPageChangeListener {
    private val viewModel: OnBoardingViewModel by inject()

    private lateinit var nextButton: Button
    private lateinit var backButton: View
    private lateinit var skipButton: View
    private lateinit var viewPager: ViewPager
    private lateinit var indicator: TabLayout

    override fun initView(view: View) {
        viewPager = view.findViewById(R.id.viewPager)
        nextButton = view.findViewById(R.id.onboarding_btn_next)
        backButton = view.findViewById(R.id.onboarding_btn_back)
        skipButton = view.findViewById(R.id.button_on_boarding_skip)
        indicator = view.findViewById(R.id.indicator)
    }

    override fun initListener() {
        viewPager.adapter = OnboardingPageAdapter(
            childFragmentManager,
            FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
        )
        indicator.setupWithViewPager(viewPager, true)
        viewPager.addOnPageChangeListener(this)

        backButton.setOnClickListener {
            val currentPage = viewPager.currentItem
            if (currentPage > 0) {
                viewPager.currentItem = currentPage - 1
            }
        }

        nextButton.setOnClickListener {
            val currentPage = viewPager.currentItem
            if (currentPage < 2) {
                viewPager.currentItem = currentPage + 1
            } else {
                viewModel.finishOnBoarding()
            }
        }

        skipButton.setOnClickListener {
            viewModel.finishOnBoarding()
        }
    }

    override fun initObserve() {
        viewModel.state.observe(this, Observer { state ->
            when (state) {
                is State.Loading -> onLoading()
                is State.Success<*> -> onSuccess(state.data as Boolean)
                is State.Failure -> onFailure()
            }
        })
    }

    private fun onLoading() {}

    private fun onSuccess(state: Boolean) {
        if (state) findNavController().navigate(R.id.action_onBoardingFragment_to_loginFragment)
        else Toast.makeText(context, "something wrong", Toast.LENGTH_SHORT).show()
    }

    private fun onFailure() {}

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

    override fun onPageSelected(position: Int) {
        nextButton.text =
            getString(if (position < 2) R.string.button_next else R.string.action_on_boarding_done)
        backButton.isVisible = position > 0
    }

    override fun onPageScrollStateChanged(state: Int) {}
}

class OnboardingPageAdapter(fm: FragmentManager, behavior: Int) :
    FragmentStatePagerAdapter(fm, behavior) {

    override fun getItem(position: Int) = OnBoardingPageFragment.newInstance(position)

    override fun getCount() = 3

}