package com.caunb163.mxh.ui.main.home.comment

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.caunb163.data.datalocal.LocalStorage
import com.caunb163.domain.model.CommentEntity
import com.caunb163.domain.model.PostEntity
import com.caunb163.domain.model.User
import com.caunb163.mxh.R
import com.caunb163.mxh.state.State
import com.caunb163.mxh.ultis.CheckPermission
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.InternalCoroutinesApi
import org.koin.android.ext.android.inject

@Suppress("UNCHECKED_CAST")
@InternalCoroutinesApi
class CommentFragment : BottomSheetDialogFragment() {
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
    private lateinit var glide: RequestManager
    private lateinit var commentAdapter: CommentAdapter
    private lateinit var user: User

    private val list = mutableListOf<CommentEntity>()
    private val args: CommentFragmentArgs by navArgs()
    private lateinit var post: PostEntity
    private var timenow: Long = 0
    private var image: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_comment, container, false)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = BottomSheetDialog(requireContext(), theme)
        dialog.setOnShowListener {

            val bottomSheetDialog = it as BottomSheetDialog
            val parentLayout =
                bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            parentLayout?.let { it1 ->
                val behaviour = BottomSheetBehavior.from(it1)
                setupFullHeight(it1)
                behaviour.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
        return dialog
    }

    private fun setupFullHeight(bottomSheet: View) {
        val layoutParams = bottomSheet.layoutParams
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
        bottomSheet.layoutParams = layoutParams
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        post = args.post
        initview(view)
        initObserve()
        addListener()
        tvLike.text = "${post.arrLike.size}"
    }

    private fun initview(view: View) {
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
        commentAdapter = CommentAdapter(glide, user)

        recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = commentAdapter
        }
    }

    private fun addListener() {
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
        }

        imvCamera.setOnClickListener {
            ensurePermission()
        }
    }

    fun initObserve() {
        viewModel.setPostId(post.postId)
        viewModel.state.observe(this, Observer { state ->
            when (state) {
                is State.Loading -> onLoading()
                is State.Success<*> -> onSuccess(state.data as MutableList<CommentEntity>)
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

    private fun onSuccess(comment: MutableList<CommentEntity>) {
        recyclerView.visibility = View.VISIBLE
        progressBar.visibility = View.INVISIBLE
        list.clear()
        list.addAll(comment)
        commentAdapter.updateData(list)
        recyclerView.scrollToPosition(list.size - 1)
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