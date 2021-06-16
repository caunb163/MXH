package com.caunb163.mxh.ui.main.home.view_image

import android.content.Intent
import android.util.Log
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
import com.caunb163.domain.model.User
import com.caunb163.mxh.R
import com.caunb163.mxh.base.BaseDialogFragment
import com.caunb163.mxh.ui.main.home.HomeOnClick
import com.caunb163.mxh.ui.main.home.HomeViewModel
import com.caunb163.mxh.ui.main.profile.ProfileViewModel
import kotlinx.coroutines.InternalCoroutinesApi
import org.koin.android.ext.android.inject

@InternalCoroutinesApi
class ListImageFragment : BaseDialogFragment(), OnImageClick, HomeOnClick {
    private val TAG = "ListImageFragment"

    private lateinit var recyclerView: RecyclerView
    private lateinit var glide: RequestManager
    private lateinit var listImageAdapter: ListImageAdapter
    private lateinit var post: Post
    private val args: ListImageFragmentArgs by navArgs()
    private val localStorage: LocalStorage by inject()
    private val repositoryUser: RepositoryUser by inject()
    private val viewModel: HomeViewModel by inject()
    private lateinit var user: User

    override fun getLayoutId(): Int = R.layout.fragment_list_image

    override fun initView(view: View) {
        recyclerView = view.findViewById(R.id.listimage_recyclerview)

        post = args.post
        glide = Glide.with(this)
        glide.applyDefaultRequestOptions(
            RequestOptions()
                .placeholder(R.drawable.image_default)
                .error(R.drawable.image_default)
        )
        user = localStorage.getAccount()!!
        listImageAdapter = ListImageAdapter(glide, this, user, this, post, repositoryUser)
        recyclerView.apply {
            layoutManager = RecyclerViewManager(requireContext())
            adapter = listImageAdapter
        }
        recyclerView.smoothScrollToPosition(args.position)
    }

    override fun initListener() {}

    override fun initObserve() {}

    override fun createPostClick() {}

    override fun onCommentClick(post: Post) {
        val action = ListImageFragmentDirections.actionListImageFragmentToCommentFragment(post)
        findNavController().navigate(action)
    }

    override fun onLikeClick(postId: String) {
        if (post.arrLike.contains(user.userId)){
            post.arrLike.remove(user.userId)
        } else {
            post.arrLike.add(user.userId)
        }
        listImageAdapter.notifyDataSetChanged()
        viewModel.likePost(postId)
    }

    override fun onShareClick(content: String) {
        val sharingIntent = Intent(Intent.ACTION_SEND)
        sharingIntent.type = "text/plain"
        sharingIntent.putExtra(Intent.EXTRA_TEXT, content)
        startActivity(Intent.createChooser(sharingIntent, "Chia sẻ nội dung"))
    }

    override fun onEditClick(post: Post) {}

    override fun onDeleteClick(post: Post) {}

    override fun onImageClick(post: Post, position: Int) {}

    override fun onAvatarClick(userId: String) {}

    override fun onViewClick(listMedia: MutableList<String>, position: Int, boolean: Boolean) {
        val action = ListImageFragmentDirections.actionListImageFragmentToImageViewFragment(listMedia.toTypedArray(), position, boolean)
        findNavController().navigate(action)
    }

}