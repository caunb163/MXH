package com.caunb163.mxh.ui.main.messenger.chat

import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.caunb163.data.datalocal.LocalStorage
import com.caunb163.data.repository.RepositoryUser
import com.caunb163.domain.model.Message
import com.caunb163.domain.model.User
import com.caunb163.mxh.MainActivity
import com.caunb163.mxh.R
import com.caunb163.mxh.base.BaseDialogFragment
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
    private lateinit var btnEmoji: ImageView
    private lateinit var progressBar: ProgressBar
    private lateinit var progressSend: ProgressBar
    private lateinit var imageView: ImageView
    private lateinit var camera: ImageView

    private val viewModel: ChatViewModel by inject()
    private val args: ChatFragmentArgs by navArgs()
    private val localStorage: LocalStorage by inject()
    private val repositoryUser: RepositoryUser by inject()

    private lateinit var chatAdapter: ChatAdapter
    private lateinit var glide: RequestManager
    private lateinit var user: User
    private val list = mutableListOf<Message>()
    private var timenow: Long = 0
    private var image: String = ""

    override fun getLayoutId(): Int = R.layout.fragment_chat

    override fun initView(view: View) {
        toolbar = view.findViewById(R.id.chat_toolbar)
        toolbar.title = args.group.name
        recyclerView = view.findViewById(R.id.chat_recyclerView)
        chat = view.findViewById(R.id.chat_chat)
        btnSend = view.findViewById(R.id.chat_send)
        btnEmoji = view.findViewById(R.id.chat_enmoji)
        progressBar = view.findViewById(R.id.chat_progressBar)
        progressSend = view.findViewById(R.id.chat_progressBar_send)
        imageView = view.findViewById(R.id.chat_image)
        camera = view.findViewById(R.id.chat_camera)

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

        chatAdapter = ChatAdapter(glide, user, repositoryUser)
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
                        btnSend.visibility = View.VISIBLE
                        btnEmoji.visibility = View.INVISIBLE
                    } else {
                        btnSend.visibility = View.INVISIBLE
                    }
                }
            }
        })

        btnSend.setOnClickListener {
            if (chat.text.toString().isNotEmpty() || image.isNotEmpty()) {
                timenow = System.currentTimeMillis()
                viewModel.createMessage(
                    chat.text.toString(),
                    timenow,
                    user.userId,
                    image
                )
                imageView.setImageDrawable(null)
                imageView.visibility = View.GONE
                image = ""
                chat.setText("")
                hideKeyboardFrom(requireContext(), it)
            }
        }

        btnEmoji.setOnClickListener {
            val action = ChatFragmentDirections.actionChatFragmentToEmojiFragment(args.group)
            findNavController().navigate(action)
        }

        camera.setOnClickListener {
            ensurePermission()
        }
    }

    override fun initObserve() {
        viewModel.setGroupId(args.group.groupId)

        viewModel.listener.observe(this, Observer { state ->
            when (state) {
                is State.Loading -> onLoadingListener()
                is State.Success<*> -> onSuccessListener(state.data as Message)
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
        btnEmoji.visibility = View.INVISIBLE
        btnSend.visibility = View.INVISIBLE
    }

    private fun onSuccessCreateMessage() {
        progressSend.visibility = View.INVISIBLE
        btnEmoji.visibility = View.VISIBLE
        btnSend.visibility = View.INVISIBLE
    }

    private fun onSuccessListener(message: Message) {
        if (message.groupId.isEmpty()) {
            val index = checkMessageIndex(message)
            if (index != -1) {
                list.removeAt(index)
                chatAdapter.notifyItemRemoved(index)
            }
        } else if (checkListAdd(message)) {
            val index = checkMessageIndex(message)
            if (index != -1) {
                list[index] = message
                chatAdapter.notifyItemChanged(index)
            }
        } else {
            list.add(message)
//            list.sortBy { it.createDate.inc() }
            chatAdapter.notifyItemInserted(list.size - 1)
        }
        recyclerView.smoothScrollToPosition(list.size)
    }

    private fun checkMessageIndex(message: Message): Int {
        for (index in 0 until list.size)
            if (list[index].messageId == message.messageId) {
                return index
            }
        return -1
    }

    private fun checkListAdd(message: Message): Boolean {
        list.forEach {
            if (it.messageId == message.messageId) {
                return true
            }
        }
        return false
    }

    private fun onFailure(message: String) {
        btnSend.visibility = View.INVISIBLE
        progressSend.visibility = View.INVISIBLE
        btnEmoji.visibility = View.VISIBLE
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
                imageView.visibility = View.VISIBLE
                glide.applyDefaultRequestOptions(RequestOptions()).load(image)
                    .into(imageView)
                btnEmoji.visibility = View.INVISIBLE
                btnSend.visibility = View.VISIBLE
            }
        }
    }
}