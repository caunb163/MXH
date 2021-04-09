package com.caunb163.mxh.ui.main.messenger.chat.emoji

import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.caunb163.data.datalocal.LocalStorage
import com.caunb163.domain.model.GroupEntity
import com.caunb163.domain.model.User
import com.caunb163.mxh.MainActivity
import com.caunb163.mxh.R
import com.caunb163.mxh.base.BaseFragment
import com.caunb163.mxh.state.State
import com.caunb163.mxh.ui.main.messenger.chat.ChatViewModel
import com.caunb163.mxh.ultis.CustomProgressBar
import org.koin.android.ext.android.inject

class EmojiPageFragment(
    private val position: Int,
    private val groupEntity: GroupEntity
) : BaseFragment(R.layout.fragment_emoji_page), OnEmojiClick {
    private val TAG = "EmojiPageFragment"

    private lateinit var recyclerView: RecyclerView
    private lateinit var list: Array<Int>
    private lateinit var glide: RequestManager
    private lateinit var emojiAdapter: EmojiAdapter
    private val viewModel: ChatViewModel by inject()
    private val localStorage: LocalStorage by inject()
    private var timenow: Long = 0
    private lateinit var user: User
//    private lateinit var customProgressBar: CustomProgressBar

    override fun initView(view: View) {
        recyclerView = view.findViewById(R.id.emoji_recyclerview)
        glide = Glide.with(this)
        glide.applyDefaultRequestOptions(
            RequestOptions()
                .placeholder(R.drawable.image_default)
                .error(R.drawable.image_default)
        )
        viewModel.setGroupId(groupEntity.groupId)
        user = localStorage.getAccount()!!
        selectList(position)
//        customProgressBar = CustomProgressBar(requireContext())

        emojiAdapter = EmojiAdapter(list, glide, this)
        recyclerView.apply {
            layoutManager = GridLayoutManager(requireContext(), 4)
            adapter = emojiAdapter
        }

    }

    override fun initListener() {
    }

    override fun initObserve() {
        viewModel.stateCreateMessage.observe(this, Observer { state ->
            when (state) {
                is State.Loading -> onLoadingSend()
                is State.Success<*> -> onSuccessCreateMessage()
                is State.Failure -> onFailure(state.message)
            }
        })
    }

    override fun onGifClick(url: Int) {
        Log.e(TAG, "onGifClick: $url")
        timenow = System.currentTimeMillis()
        viewModel.createMessage(
            "", timenow, user.userId, url.toString()
        )
        (activity as MainActivity).onBackPressed()
    }

    private fun selectList(position: Int) {
        when (position) {
            0 -> {
                list = arrayOf(
                    R.raw.qoobee_1,
                    R.raw.qoobee_2,
                    R.raw.qoobee_3,
                    R.raw.qoobee_4,
                    R.raw.qoobee_5,
                    R.raw.qoobee_6,
                    R.raw.qoobee_7,
                    R.raw.qoobee_8,
                    R.raw.qoobee_9,
                    R.raw.qoobee_10,
                    R.raw.qoobee_11,
                    R.raw.qoobee_12,
                    R.raw.qoobee_13,
                    R.raw.qoobee_14,
                    R.raw.qoobee_15,
                    R.raw.qoobee_16,
                    R.raw.qoobee_17,
                    R.raw.qoobee_18,
                    R.raw.qoobee_19,
                    R.raw.qoobee_20,
                    R.raw.qoobee_21,
                    R.raw.qoobee_22,
                    R.raw.qoobee_23,
                    R.raw.qoobee_24,
                )
            }

            1 -> {
                list = arrayOf(
                    R.raw.chummy_1,
                    R.raw.chummy_2,
                    R.raw.chummy_3,
                    R.raw.chummy_4,
                    R.raw.chummy_5,
                    R.raw.chummy_6,
                    R.raw.chummy_7,
                    R.raw.chummy_8,
                    R.raw.chummy_9,
                    R.raw.chummy_10,
                    R.raw.chummy_11,
                    R.raw.chummy_12,
                    R.raw.chummy_13,
                    R.raw.chummy_14,
                    R.raw.chummy_15,
                    R.raw.chummy_16,
                )
            }
        }
    }

    private fun onLoadingSend() {
//        customProgressBar.show()
    }

    private fun onSuccessCreateMessage() {
//        customProgressBar.dismiss()
//        (activity as MainActivity).onBackPressed()
    }

    private fun onFailure(message: String) {
//        customProgressBar.dismiss()
        showToast(message)
    }

}

//class GridSpacingItemDecoration(spacing: Int, spanCount: Int) : RecyclerView.ItemDecoration() {
//    private val spacing = spacing
//    private val spanCout = spanCount
//
//    override fun getItemOffsets(
//        outRect: Rect,
//        view: View,
//        parent: RecyclerView,
//        state: RecyclerView.State
//    ) {
//        outRect.top = spacing
//        outRect.bottom = spacing
//        outRect.right = spacing
//
//        if (parent.getChildAdapterPosition(view) < spanCout){
//            outRect.left = spacing
//        } else outRect.left = 0
//
//    }
//}

