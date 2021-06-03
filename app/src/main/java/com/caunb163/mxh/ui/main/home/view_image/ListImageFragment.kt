package com.caunb163.mxh.ui.main.home.view_image

import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.caunb163.data.datalocal.LocalStorage
import com.caunb163.data.repository.RepositoryUser
import com.caunb163.domain.model.Post
import com.caunb163.mxh.R
import com.caunb163.mxh.base.BaseDialogFragment
import com.caunb163.mxh.ui.main.home.HomeOnClick
import org.koin.android.ext.android.inject

class ListImageFragment : BaseDialogFragment(), OnImageClick, HomeOnClick {

    private lateinit var recyclerView: RecyclerView
    private lateinit var glide: RequestManager
    private lateinit var listImageAdapter: ListImageAdapter
    private lateinit var postEntity: Post
    private val args: ListImageFragmentArgs by navArgs()
    private val localStorage: LocalStorage by inject()
    private val repositoryUser: RepositoryUser by inject()

    override fun getLayoutId(): Int = R.layout.fragment_list_image

    override fun initView(view: View) {
        recyclerView = view.findViewById(R.id.listimage_recyclerview)

        postEntity = args.post
        glide = Glide.with(this)
        glide.applyDefaultRequestOptions(
            RequestOptions()
                .placeholder(R.drawable.image_default)
                .error(R.drawable.image_default)
        )

        listImageAdapter = ListImageAdapter(glide, this, localStorage.getAccount()!!, this, postEntity, repositoryUser)
        recyclerView.apply {
            layoutManager = RecyclerViewManager(requireContext())
            adapter = listImageAdapter
        }
        recyclerView.smoothScrollToPosition(args.position)
    }

    override fun initListener() {}

    override fun initObserve() {}

    override fun createPostClick() {}

    override fun onCommentClick(post: Post) {}

    override fun onLikeClick(postId: String) {}

    override fun onShareClick(content: String) {}

    override fun onEditClick(post: Post) {}

    override fun onDeleteClick(post: Post) {}

    override fun onImageClick(post: Post, position: Int) {}

    override fun onViewClick(listMedia: MutableList<String>, position: Int, boolean: Boolean) {
        val action = ListImageFragmentDirections.actionListImageFragmentToImageViewFragment(listMedia.toTypedArray(), position, boolean)
        findNavController().navigate(action)
    }

}