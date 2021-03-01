package com.caunb163.mxh.ui.main.home.comment

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.caunb163.data.datalocal.LocalStorage
import com.caunb163.domain.model.CommentEntity
import com.caunb163.domain.model.User
import com.caunb163.mxh.R
import com.caunb163.mxh.state.State
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.InternalCoroutinesApi
import org.koin.android.ext.android.inject

@Suppress("UNCHECKED_CAST")
@InternalCoroutinesApi
class CommentFragment : BottomSheetDialogFragment() {
    private val TAG = "CommentFragment"

    private lateinit var imvLike: ImageView
    private lateinit var imvCamera: ImageView
    private lateinit var imvSend: ImageView
    private lateinit var edtComment: EditText
    private lateinit var recyclerView: RecyclerView

    private val viewModel: CommentViewModel by inject()
    private val localStorage: LocalStorage by inject()
    private lateinit var glide: RequestManager
    private lateinit var commentAdapter: CommentAdapter
    private lateinit var user: User

    private val list = mutableListOf<CommentEntity>()
    val args: CommentFragmentArgs by navArgs()
    private lateinit var postId: String

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
        postId = args.postId
        Log.e(TAG, "onViewCreated: $postId")
        initview(view)
        initObserve()
    }

    private fun initview(view: View) {
        imvLike = view.findViewById(R.id.cmt_imv_like)
        imvCamera = view.findViewById(R.id.cmt_imv_camera)
        imvSend = view.findViewById(R.id.cmt_imv_send)
        edtComment = view.findViewById(R.id.cmt_edt_cmt)
        recyclerView = view.findViewById(R.id.cmt_recycler_view)

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

    fun initObserve() {
        viewModel.state.observe(this, Observer { state ->
            when (state) {
                is State.Loading -> onLoading()
                is State.Success<*> -> onSuccess(state.data as MutableList<CommentEntity>)
                is State.Failure -> onFailure(state.message)
            }
        })
    }

    private fun onLoading() {
    }

    private fun onSuccess(comment: MutableList<CommentEntity>) {
        list.clear()
        list.addAll(comment)
        commentAdapter.updateData(list)
    }

    private fun onFailure(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}