package com.caunb163.mxh.ui.main.messenger.chat

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.caunb163.data.datalocal.LocalStorage
import com.caunb163.domain.model.MessageEntity
import com.caunb163.domain.model.User
import com.caunb163.mxh.MainActivity
import com.caunb163.mxh.R
import com.caunb163.mxh.base.BaseFragment
import com.caunb163.mxh.state.State
import com.google.android.material.appbar.MaterialToolbar
import org.koin.android.ext.android.inject

@Suppress("UNCHECKED_CAST")
class ChatFragment : BaseFragment(R.layout.fragment_chat) {
    private val TAG = "ChatFragment"
    private lateinit var toolbar: MaterialToolbar
    private lateinit var recyclerView: RecyclerView
    private lateinit var chat: EditText
    private lateinit var btnSend: ImageView
    private lateinit var progressBar: ProgressBar
    private lateinit var progressSend: ProgressBar

    private val viewModel: ChatViewModel by inject()
    private val args: ChatFragmentArgs by navArgs()
    private val localStorage: LocalStorage by inject()

    private lateinit var chatAdapter: ChatAdapter
    private lateinit var glide: RequestManager
    private lateinit var user: User
    private val list = mutableListOf<MessageEntity>()
    private var timenow: Long = 0

    override fun initView(view: View) {
        toolbar = view.findViewById(R.id.chat_toolbar)
        toolbar.title = args.group.name
        recyclerView = view.findViewById(R.id.chat_recyclerView)
        chat = view.findViewById(R.id.chat_chat)
        btnSend = view.findViewById(R.id.chat_send)
        progressBar = view.findViewById(R.id.chat_progressBar)
        progressSend = view.findViewById(R.id.chat_progressBar_send)

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        toolbar.setNavigationOnClickListener {
            (activity as MainActivity).onBackPressed()
        }
        glide = Glide.with(this)
        glide.applyDefaultRequestOptions(
            RequestOptions()
                .placeholder(R.drawable.image_default)
                .error(R.drawable.image_default)
        )
        user = localStorage.getAccount()!!

        chatAdapter = ChatAdapter(glide, user)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = chatAdapter
        }
    }

    override fun initListener() {
        chat.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                s?.let {
                    if (it.isNotEmpty()) {
                        btnSend.isEnabled = true
                        btnSend.setImageResource(R.drawable.ic_send)
                    } else {
                        btnSend.isEnabled = false
                        btnSend.setImageResource(R.drawable.ic_un_send)
                    }
                }
            }
        })

        btnSend.setOnClickListener {
            timenow = System.currentTimeMillis()
            viewModel.createMessage(chat.text.toString(), timenow, args.group.groupId, user.userId)
        }
    }

    override fun initObserve() {
        viewModel.setGroupId(args.group.groupId)
        viewModel.getAllMessage()
        viewModel.state.observe(this, Observer { state ->
            when (state) {
                is State.Loading -> onLoading()
                is State.Success<*> -> onSuccess(state.data as MutableList<MessageEntity>)
                is State.Failure -> onFailure(state.message)
            }
        })
        viewModel.stateCreateMessage.observe(this, Observer { state ->
            when (state) {
                is State.Loading -> onLoadingSend()
                is State.Success<*> -> onSuccessCreateMessage()
                is State.Failure -> onFailure(state.message)
            }
        })
    }

    private fun onLoadingListener(){}

    private fun onLoading() {
        progressBar.visibility = View.VISIBLE
        recyclerView.visibility = View.INVISIBLE
    }

    private fun onLoadingSend() {
        progressSend.visibility = View.VISIBLE
        btnSend.visibility = View.INVISIBLE
    }

    private fun onSuccess(listMessage: MutableList<MessageEntity>) {
        progressBar.visibility = View.INVISIBLE
        recyclerView.visibility = View.VISIBLE
        list.clear()
        list.addAll(listMessage)
        chatAdapter.updateData(list)
        recyclerView.smoothScrollToPosition(list.size - 1)
        viewModel.listener.observe(this, Observer { state ->
            when (state) {
                is State.Loading -> onLoadingListener()
                is State.Success<*> -> onSuccessListener(state.data as MessageEntity)
                is State.Failure -> onFailure(state.message)
            }
        })
    }

    private fun onSuccessCreateMessage() {
        progressSend.visibility = View.INVISIBLE
        btnSend.visibility = View.VISIBLE
        progressBar.visibility = View.INVISIBLE
        recyclerView.visibility = View.VISIBLE
        chat.setText("")
    }

    private fun onSuccessListener(message: MessageEntity) {
        progressSend.visibility = View.INVISIBLE
        btnSend.visibility = View.VISIBLE
        if (message.groupId.isEmpty()) {
            Log.e(TAG, "onSuccessListener: remove")
            val index = checkMessageIndex(message)
            if (index != -1) {
                list.removeAt(index)
                chatAdapter.notifyItemRemoved(index)
            }
        } else if (checkListAdd(message)) {
            Log.e(TAG, "onSuccessListener: update")
            val index = checkMessageIndex(message)
            if (index != -1) {
                list[index] = message
                chatAdapter.notifyItemChanged(index)
            }
        } else {
            if (!list.contains(message)) {
                Log.e(TAG, "onSuccessListener: add")
                list.add(message)
                chatAdapter.notifyItemInserted(list.size - 1)
            }
        }
        recyclerView.smoothScrollToPosition(list.size - 1)
    }

    private fun checkMessageIndex(message: MessageEntity): Int {
        for (index in 1 until list.size)
            if (list[index].messageId == message.messageId) {
                return index
            }
        return -1
    }

    private fun checkListAdd(message: MessageEntity): Boolean {
        list.forEach {
            if (it.messageId == message.messageId) {
                return true
            }
        }
        return false
    }

    private fun onFailure(message: String) {
        progressBar.visibility = View.INVISIBLE
        recyclerView.visibility = View.VISIBLE
        progressSend.visibility = View.INVISIBLE
        btnSend.visibility = View.VISIBLE
        showToat(message)
    }
}