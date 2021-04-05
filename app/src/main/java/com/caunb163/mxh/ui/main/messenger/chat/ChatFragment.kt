package com.caunb163.mxh.ui.main.messenger.chat

import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
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
import com.caunb163.mxh.base.BaseDialogFragment
import com.caunb163.mxh.base.BaseFragment
import com.caunb163.mxh.state.State
import com.caunb163.mxh.ultis.CheckPermission
import com.google.android.material.appbar.MaterialToolbar
import org.koin.android.ext.android.inject

@Suppress("UNCHECKED_CAST")
class ChatFragment : BaseDialogFragment() {
    private val TAG = "ChatFragment"
    private val REQUEST_INTENT_IMAGE_CODE = 2314

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
    private var image: String = ""

    override fun getLayoutId(): Int = R.layout.fragment_chat

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
        chatAdapter.updateData(list)
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
            viewModel.createMessage(
                chat.text.toString(),
                timenow,
                args.group.groupId,
                user.userId,
                ""
            )
            chat.setText("")
            hideKeyboardFrom(requireContext(), it)
        }
    }

    override fun initObserve() {
        viewModel.setGroupId(args.group.groupId)

        viewModel.listener.observe(this, Observer { state ->
            when (state) {
                is State.Loading -> onLoadingListener()
                is State.Success<*> -> onSuccessListener(state.data as MessageEntity)
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

    private fun onLoadingListener() {}

    private fun onLoadingSend() {
        progressSend.visibility = View.VISIBLE
        btnSend.visibility = View.INVISIBLE
    }

    private fun onSuccessCreateMessage() {
        progressSend.visibility = View.INVISIBLE
        btnSend.visibility = View.VISIBLE
        progressBar.visibility = View.INVISIBLE
        recyclerView.visibility = View.VISIBLE
    }

    private fun onSuccessListener(message: MessageEntity) {
        progressSend.visibility = View.INVISIBLE
        btnSend.visibility = View.VISIBLE
        progressBar.visibility = View.INVISIBLE
        recyclerView.visibility = View.VISIBLE
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
                list.sortBy { it.createDate.inc() }
                chatAdapter.notifyDataSetChanged()
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
        showToast(message)
    }

    private fun ensurePermission() {
        if (CheckPermission.checkPermission(requireContext())) {
            openLibrary()
        } else
            requestPermissions(
                CheckPermission.listPermission.toTypedArray(),
                REQUEST_INTENT_IMAGE_CODE
            )
    }

    private fun openLibrary() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_PICK
        startActivityForResult(intent, REQUEST_INTENT_IMAGE_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_INTENT_IMAGE_CODE) {
            if (CheckPermission.checkPermission(requireContext())) {
                openLibrary()
            } else Toast.makeText(context, "Yêu cầu bị từ chối", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_INTENT_IMAGE_CODE) {
            data?.let {
                image = data.data!!.toString()
//                imvImage.visibility = View.VISIBLE
//                glide.applyDefaultRequestOptions(RequestOptions()).load(image)
//                    .into(imvImage)
            }
        }
    }
}