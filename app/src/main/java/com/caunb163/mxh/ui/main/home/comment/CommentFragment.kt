package com.caunb163.mxh.ui.main.home.comment

import android.content.Intent
import android.view.View
import android.widget.*
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.caunb163.data.datalocal.LocalStorage
import com.caunb163.data.repository.RepositoryUser
import com.caunb163.domain.model.Comment
import com.caunb163.domain.model.Post
import com.caunb163.domain.model.User
import com.caunb163.mxh.R
import com.caunb163.mxh.base.BaseDialogFragment
import com.caunb163.mxh.state.State
import com.caunb163.mxh.ultis.CheckPermission
import kotlinx.coroutines.InternalCoroutinesApi
import org.koin.android.ext.android.inject

@Suppress("UNCHECKED_CAST")
@InternalCoroutinesApi
class CommentFragment : BaseDialogFragment() {
    private val TAG = "CommentFragment"
    private val REQUEST_INTENT_IMAGE_CODE = 1234

    private lateinit var imvLike: ImageView
    private lateinit var imvCamera: ImageView
    private lateinit var imvSend: ImageView
    private lateinit var imvImage: ImageView
    private lateinit var edtComment: EditText
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var progressBarCmt: ProgressBar
    private lateinit var tvLike: TextView

    private val viewModel: CommentViewModel by inject()
    private val localStorage: LocalStorage by inject()
    private val repositoryUser: RepositoryUser by inject()
    private lateinit var glide: RequestManager
    private lateinit var commentAdapter: CommentAdapter
    private lateinit var user: User

    private val list = mutableListOf<Comment>()
    private val args: CommentFragmentArgs by navArgs()
    private lateinit var post: Post
    private var timenow: Long = 0
    private var image: String = ""
    override fun getLayoutId(): Int = R.layout.fragment_comment

    override fun initView(view: View) {
        imvLike = view.findViewById(R.id.cmt_imv_like)
        imvCamera = view.findViewById(R.id.cmt_imv_camera)
        imvSend = view.findViewById(R.id.cmt_imv_send)
        imvImage = view.findViewById(R.id.cmt_image)
        edtComment = view.findViewById(R.id.cmt_edt_cmt)
        recyclerView = view.findViewById(R.id.cmt_recycler_view)
        progressBar = view.findViewById(R.id.cmt_progressbar)
        progressBarCmt = view.findViewById(R.id.cmt_progressbar_cmt)
        tvLike = view.findViewById(R.id.cmt_tv_like)

        glide = Glide.with(this)
        glide.applyDefaultRequestOptions(
            RequestOptions()
                .placeholder(R.drawable.image_default)
                .error(R.drawable.image_default)
        )
        user = localStorage.getAccount()!!
        commentAdapter = CommentAdapter(glide, repositoryUser)

        recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = commentAdapter
        }
        commentAdapter.updateData(list)
        post = args.post
        tvLike.text = "${post.arrLike.size}"
    }

    override fun initListener() {
        imvSend.setOnClickListener {
            if (edtComment.text.isNotEmpty() || image.isNotEmpty()) {
                timenow = System.currentTimeMillis()
                viewModel.createComment(
                    user.userId,
                    timenow,
                    post.postId,
                    image,
                    edtComment.text.toString()
                )
                image = ""
                imvImage.visibility = View.GONE
                edtComment.setText("")
            }
            hideKeyboardFrom(requireContext(), it)
        }

        imvCamera.setOnClickListener {
            ensurePermission()
        }
    }

    override fun initObserve() {
        viewModel.setPostId(post.postId)
        viewModel.state.observe(this, Observer { state ->
            when (state) {
                is State.Loading -> onLoading()
                is State.Success<*> -> onSuccessListener(state.data as Comment)
                is State.Failure -> onFailure(state.message)
            }
        })
        viewModel.stateCmt.observe(this, Observer { state ->
            when (state) {
                is State.Loading -> onLoadingCmt()
                is State.Success<*> -> onSuccess()
                is State.Failure -> onFailure(state.message)
            }
        })
    }

    private fun onLoading() {
        if (list.isNotEmpty()) {
            recyclerView.visibility = View.INVISIBLE
            progressBar.visibility = View.VISIBLE
        }
    }

    private fun onLoadingCmt() {
        imvSend.visibility = View.INVISIBLE
        progressBarCmt.visibility = View.VISIBLE
    }

    private fun onSuccess() {
        imvSend.visibility = View.VISIBLE
        progressBarCmt.visibility = View.INVISIBLE
    }

    private fun onSuccessListener(comment: Comment) {
        recyclerView.visibility = View.VISIBLE
        progressBar.visibility = View.INVISIBLE
        if (comment.postId.isEmpty()) {
            val index = checkCommentIndex(comment)
            if (index != -1) {
                list.removeAt(index)
                commentAdapter.notifyItemRemoved(index)
            }
        } else if (checkListAdd(comment)) {
            val index = checkCommentIndex(comment)
            if (index != -1) {
                list[index] = comment
                commentAdapter.notifyItemChanged(index)
            }
        } else {
            list.add(comment)
            commentAdapter.notifyItemInserted(list.size - 1)
        }
        recyclerView.scrollToPosition(list.size - 1)
    }

    private fun checkCommentIndex(comment: Comment): Int {
        for (index in 0 until list.size)
            if (list[index].commentId == comment.commentId) {
                return index
            }
        return -1
    }

    private fun checkListAdd(comment: Comment): Boolean {
        list.forEach {
            if (it.commentId == comment.commentId) {
                return true
            }
        }
        return false
    }

    private fun onFailure(message: String) {
        imvSend.visibility = View.VISIBLE
        progressBarCmt.visibility = View.INVISIBLE
        recyclerView.visibility = View.VISIBLE
        progressBar.visibility = View.INVISIBLE
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
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
                imvImage.visibility = View.VISIBLE
                glide.applyDefaultRequestOptions(RequestOptions()).load(image)
                    .into(imvImage)
            }
        }
    }
}