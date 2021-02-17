package com.caunb163.mxh.ui.onboarding

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.caunb163.mxh.R

class OnBoardingPageFragment : Fragment(R.layout.fragment_on_boarding_page) {
    companion object {
        const val ARG_POSITION = "position"
        fun newInstance(positon: Int): OnBoardingPageFragment {
            return OnBoardingPageFragment().apply {
                arguments = bundleOf(
                    ARG_POSITION to positon
                )
            }
        }

        private val imgeRes =
            arrayOf(R.drawable.image1, R.drawable.image2, R.drawable.image3)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val onboardingStrings = resources.getStringArray(R.array.onboarding_text)
        val onboardingStringDes = resources.getStringArray(R.array.onboarding_description)
        arguments?.let {
            val position = it.getInt(ARG_POSITION)
            view.findViewById<TextView>(R.id.onboarding_text).text = onboardingStrings[position]
            view.findViewById<TextView>(R.id.onboarding_text_des).text =
                onboardingStringDes[position]
            view.findViewById<ImageView>(R.id.onboarding_image).setImageResource(imgeRes[position])
        }
    }
}