package com.caunb163.mxh.ui.main.home.edit_post

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.caunb163.data.datalocal.LocalStorage
import com.caunb163.data.repository.RepositoryUser
import com.caunb163.domain.model.Post
import com.caunb163.mxh.R
import com.caunb163.mxh.base.BaseDialogFragment
import com.caunb163.mxh.state.State
import com.caunb163.mxh.ultis.CheckPermission
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.*
import org.koin.android.ext.android.inject

class EditPostFragment : BaseDialogFragment() {
    private val TAG = "EditPostFragment"

    private val REQUEST_INTENT_CODE_MULTIPLE = 1678
    private val REQUEST_PICK_VIDEO = 3452

    private lateinit var toolbar: Toolbar
    private lateinit var btnSave: Button
    private lateinit var imgAvatar: CircleImageView
    private lateinit var username: TextView
    private lateinit var createDate: TextView
    private lateinit var content: TextView

    private lateinit var ctl1: ConstraintLayout
    private lateinit var ctl2: ConstraintLayout
    private lateinit var ctl3: ConstraintLayout
    private lateinit var ctl4: ConstraintLayout
    private lateinit var ctl5: ConstraintLayout
    private lateinit var ctl6: ConstraintLayout
    private lateinit var ctl7: ConstraintLayout
    private lateinit var ctl8: ConstraintLayout
    private lateinit var ctl9: ConstraintLayout
    private lateinit var ctl10: ConstraintLayout

    private lateinit var img1: ImageView
    private lateinit var img21: ImageView
    private lateinit var img22: ImageView
    private lateinit var img31: ImageView
    private lateinit var img32: ImageView
    private lateinit var img33: ImageView
    private lateinit var img41: ImageView
    private lateinit var img42: ImageView
    private lateinit var img43: ImageView
    private lateinit var img44: ImageView
    private lateinit var img51: ImageView
    private lateinit var img52: ImageView
    private lateinit var img53: ImageView
    private lateinit var img54: ImageView
    private lateinit var img55: ImageView
    private lateinit var tvImageLoadMore: TextView
    private lateinit var imvAddImage: ImageView
    private lateinit var imvAddVideo: ImageView
    private lateinit var progressBar: ProgressBar

    private lateinit var videoView1: ImageView
    private lateinit var videoView21: ImageView
    private lateinit var videoView22: ImageView
    private lateinit var videoView31: ImageView
    private lateinit var videoView32: ImageView
    private lateinit var videoView33: ImageView
    private lateinit var videoView41: ImageView
    private lateinit var videoView42: ImageView
    private lateinit var videoView43: ImageView
    private lateinit var videoView44: ImageView
    private lateinit var videoView51: ImageView
    private lateinit var videoView52: ImageView
    private lateinit var videoView53: ImageView
    private lateinit var videoView54: ImageView
    private lateinit var videoView55: ImageView
    private lateinit var tvVideoLoadMore: TextView

    private lateinit var glide: RequestManager
    private val args: EditPostFragmentArgs by navArgs()
    private val viewModel: EditPostViewModel by inject()
    private val localStorage: LocalStorage by inject()
    private val repositoryUser: RepositoryUser by inject()
    private lateinit var post: Post
    private val listImages = mutableListOf<String>()
    private var videoPath = ""
    private var isVideo: Boolean = false
    private val job = Job()

    override fun getLayoutId(): Int = R.layout.fragment_edit_post

    override fun initView(view: View) {
        toolbar = view.findViewById(R.id.editpost_toolbar)
        btnSave = view.findViewById(R.id.editpost_btnSave)
        imgAvatar = view.findViewById(R.id.editpost_imv_avatar)
        username = view.findViewById(R.id.editpost_tv_user)
        createDate = view.findViewById(R.id.editpost_tv_date)
        content = view.findViewById(R.id.editpost_edt_content)

        ctl1 = view.findViewById(R.id.post_image1)
        ctl2 = view.findViewById(R.id.post_image2)
        ctl3 = view.findViewById(R.id.post_image3)
        ctl4 = view.findViewById(R.id.post_image4)
        ctl5 = view.findViewById(R.id.post_image5)
        ctl6 = view.findViewById(R.id.post_video1)
        ctl7 = view.findViewById(R.id.post_video2)
        ctl8 = view.findViewById(R.id.post_video3)
        ctl9 = view.findViewById(R.id.post_video4)
        ctl10 = view.findViewById(R.id.post_video5)

        img1 = view.findViewById(R.id.media_1_image)
        img21 = view.findViewById(R.id.media_2_image_1)
        img22 = view.findViewById(R.id.media_2_image_2)
        img31 = view.findViewById(R.id.media_3_image_1)
        img32 = view.findViewById(R.id.media_3_image_2)
        img33 = view.findViewById(R.id.media_3_image_3)
        img41 = view.findViewById(R.id.media_4_image_1)
        img42 = view.findViewById(R.id.media_4_image_2)
        img43 = view.findViewById(R.id.media_4_image_3)
        img44 = view.findViewById(R.id.media_4_image_4)
        img51 = view.findViewById(R.id.media_5_image_1)
        img52 = view.findViewById(R.id.media_5_image_2)
        img53 = view.findViewById(R.id.media_5_image_3)
        img54 = view.findViewById(R.id.media_5_image_4)
        img55 = view.findViewById(R.id.media_5_image_5)
        tvImageLoadMore = view.findViewById(R.id.media_5_load_more)

        videoView1 = view.findViewById(R.id.media_1_video)
        videoView21 = view.findViewById(R.id.media_2_video_1)
        videoView22 = view.findViewById(R.id.media_2_video_2)
        videoView31 = view.findViewById(R.id.media_3_video_1)
        videoView32 = view.findViewById(R.id.media_3_video_2)
        videoView33 = view.findViewById(R.id.media_3_video_3)
        videoView41 = view.findViewById(R.id.media_4_video_1)
        videoView42 = view.findViewById(R.id.media_4_video_2)
        videoView43 = view.findViewById(R.id.media_4_video_3)
        videoView44 = view.findViewById(R.id.media_4_video_4)
        videoView51 = view.findViewById(R.id.media_5_video_1)
        videoView52 = view.findViewById(R.id.media_5_video_2)
        videoView53 = view.findViewById(R.id.media_5_video_3)
        videoView54 = view.findViewById(R.id.media_5_video_4)
        videoView55 = view.findViewById(R.id.media_5_video_5)
        tvVideoLoadMore = view.findViewById(R.id.media_5_video_loadmore)

        imvAddImage = view.findViewById(R.id.editpost_addimage)
        imvAddVideo = view.findViewById(R.id.editpost_addvideo)
        progressBar = view.findViewById(R.id.editpost_progressbar)

        showImage(0)
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        toolbar.setNavigationOnClickListener {
            dialog?.dismiss()
        }
        glide = Glide.with(this)
        glide.applyDefaultRequestOptions(
            RequestOptions()
                .placeholder(R.drawable.image_default)
                .error(R.drawable.image_default)
        )
        post = args.post
        fillInformation()
    }

    override fun initListener() {
        content.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                s?.let {
                    if (it.isEmpty()) {
                        btnSave.isEnabled = false
                        btnSave.setBackgroundColor(resources.getColor(R.color.colorUnPost, null))
                    } else {
                        btnSave.isEnabled = true
                        btnSave.setBackgroundColor(resources.getColor(R.color.colorPost, null))
                    }
                }
            }
        })

        btnSave.setOnClickListener {
            val mPost = Post(
                userId = post.userId,
                content = content.text.toString(),
                createDate = post.createDate,
                images = listImages,
                arrCmtId = post.arrCmtId,
                arrLike = post.arrLike,
                video = videoPath,
                active = post.active
            )
            mPost.isAds = localStorage.getAccount()!!.ads
            mPost.postId = post.postId
            viewModel.editPost(mPost)
            hideKeyboardFrom(requireContext(), it)
        }

        imvAddImage.setOnClickListener { ensurePermission() }

        imvAddVideo.setOnClickListener { selectVideo() }
    }

    override fun initObserve() {
        viewModel.stateEdit.observe(this, androidx.lifecycle.Observer { state ->
            when (state) {
                is State.Loading -> onLoading()
                is State.Success<*> -> onSuccess()
                is State.Failure -> onFailure(state.message)
            }
        })
    }

    private fun fillInformation() {
        val uiScope = CoroutineScope(Dispatchers.Main + job)
        uiScope.launch(Dispatchers.IO){
            withContext(Dispatchers.Main){
                val mUser = repositoryUser.getUser(post.userId)
                username.text = mUser.username
                glide.applyDefaultRequestOptions(RequestOptions()).load(mUser.photoUrl).into(imgAvatar)
            }
        }

        createDate.text = getDate(post.createDate)
        content.text = post.content
        if (post.video.isNotEmpty()){
            videoPath = post.video
            isVideo = true
        }
        listImages.addAll(post.images)
        updateImageVideo()
    }

    private fun onLoading() {
        btnSave.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
    }

    private fun onSuccess() {
        btnSave.visibility = View.VISIBLE
        progressBar.visibility = View.INVISIBLE
        Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
        dialog?.dismiss()
    }

    private fun onFailure(message: String) {
        btnSave.visibility = View.VISIBLE
        progressBar.visibility = View.INVISIBLE
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private fun updateImageVideo() {
        if (isVideo) {
            when (listImages.size) {
                0 -> showImage(6)
                1 -> showImage(7)
                2 -> showImage(8)
                3 -> showImage(9)
                else -> showImage(10)
            }
        } else {
            when (listImages.size) {
                0 -> showImage(0)
                1 -> showImage(1)
                2 -> showImage(2)
                3 -> showImage(3)
                4 -> showImage(4)
                else -> showImage(5)
            }
        }

    }

    @SuppressLint("SetTextI18n")
    private fun showImage(size: Int) {
        ctl1.visibility = View.GONE
        ctl2.visibility = View.GONE
        ctl3.visibility = View.GONE
        ctl4.visibility = View.GONE
        ctl5.visibility = View.GONE
        ctl6.visibility = View.GONE
        ctl7.visibility = View.GONE
        ctl8.visibility = View.GONE
        ctl9.visibility = View.GONE
        ctl10.visibility = View.GONE
        tvImageLoadMore.visibility = View.GONE
        tvVideoLoadMore.visibility = View.GONE

        when (size) {
            1 -> {
                ctl1.visibility = View.VISIBLE
                glide.applyDefaultRequestOptions(RequestOptions()).load(listImages[0]).into(img1)
            }

            2 -> {
                ctl2.visibility = View.VISIBLE
                glide.applyDefaultRequestOptions(RequestOptions()).load(listImages[0]).into(img21)
                glide.applyDefaultRequestOptions(RequestOptions()).load(listImages[1]).into(img22)
            }

            3 -> {
                ctl3.visibility = View.VISIBLE
                glide.applyDefaultRequestOptions(RequestOptions()).load(listImages[0]).into(img31)
                glide.applyDefaultRequestOptions(RequestOptions()).load(listImages[1]).into(img32)
                glide.applyDefaultRequestOptions(RequestOptions()).load(listImages[2]).into(img33)
            }

            4 -> {
                ctl4.visibility = View.VISIBLE
                glide.applyDefaultRequestOptions(RequestOptions()).load(listImages[0]).into(img41)
                glide.applyDefaultRequestOptions(RequestOptions()).load(listImages[1]).into(img42)
                glide.applyDefaultRequestOptions(RequestOptions()).load(listImages[2]).into(img43)
                glide.applyDefaultRequestOptions(RequestOptions()).load(listImages[3]).into(img44)
            }

            5 -> {
                ctl5.visibility = View.VISIBLE
                glide.applyDefaultRequestOptions(RequestOptions()).load(listImages[0]).into(img51)
                glide.applyDefaultRequestOptions(RequestOptions()).load(listImages[1]).into(img52)
                glide.applyDefaultRequestOptions(RequestOptions()).load(listImages[2]).into(img53)
                glide.applyDefaultRequestOptions(RequestOptions()).load(listImages[3]).into(img54)
                glide.applyDefaultRequestOptions(RequestOptions()).load(listImages[4]).into(img55)

                if (listImages.size == 5) tvImageLoadMore.visibility = View.INVISIBLE
                else {
                    tvImageLoadMore.visibility = View.VISIBLE
                    tvImageLoadMore.text = "+${listImages.size - 5}"
                }
            }

            6 -> {
                ctl6.visibility = View.VISIBLE
                glide.applyDefaultRequestOptions(RequestOptions()).load(videoPath).into(videoView1)
            }

            7 -> {
                ctl7.visibility = View.VISIBLE
                glide.applyDefaultRequestOptions(RequestOptions()).load(videoPath).into(videoView21)
                glide.applyDefaultRequestOptions(RequestOptions()).load(listImages[0]).into(videoView22)
            }

            8 -> {
                ctl8.visibility = View.VISIBLE
                glide.applyDefaultRequestOptions(RequestOptions()).load(videoPath).into(videoView31)
                glide.applyDefaultRequestOptions(RequestOptions()).load(listImages[0]).into(videoView32)
                glide.applyDefaultRequestOptions(RequestOptions()).load(listImages[1]).into(videoView33)
            }

            9 -> {
                ctl9.visibility = View.VISIBLE
                glide.applyDefaultRequestOptions(RequestOptions()).load(videoPath).into(videoView41)
                glide.applyDefaultRequestOptions(RequestOptions()).load(listImages[0]).into(videoView42)
                glide.applyDefaultRequestOptions(RequestOptions()).load(listImages[1]).into(videoView43)
                glide.applyDefaultRequestOptions(RequestOptions()).load(listImages[2]).into(videoView44)
            }

            10 -> {
                ctl10.visibility = View.VISIBLE
                glide.applyDefaultRequestOptions(RequestOptions()).load(videoPath).into(videoView51)
                glide.applyDefaultRequestOptions(RequestOptions()).load(listImages[0]).into(videoView52)
                glide.applyDefaultRequestOptions(RequestOptions()).load(listImages[1]).into(videoView53)
                glide.applyDefaultRequestOptions(RequestOptions()).load(listImages[2]).into(videoView54)
                glide.applyDefaultRequestOptions(RequestOptions()).load(listImages[3]).into(videoView55)

                if (listImages.size == 4) tvVideoLoadMore.visibility = View.INVISIBLE
                else {
                    tvVideoLoadMore.visibility = View.VISIBLE
                    tvVideoLoadMore.text = "+${listImages.size - 4}"
                }
            }

            else -> { }
        }
    }

    private fun ensurePermission() {
        if (CheckPermission.checkPermission(requireContext())) {
            openLibraryMultiple()
        } else
            requestPermissions(
                CheckPermission.listPermission.toTypedArray(),
                REQUEST_INTENT_CODE_MULTIPLE
            )
    }

    private fun selectVideo() {
        if (CheckPermission.checkPermission(requireContext())) {
            val intent = Intent()
            intent.type = "video/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Select Video"), REQUEST_PICK_VIDEO)
        } else requestPermissions(
            CheckPermission.listPermission.toTypedArray(),
            REQUEST_PICK_VIDEO
        )
    }

    private fun openLibraryMultiple() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        startActivityForResult(
            Intent.createChooser(intent, "Select picture"),
            REQUEST_INTENT_CODE_MULTIPLE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_INTENT_CODE_MULTIPLE) {
            if (CheckPermission.checkPermission(requireContext())) {
                openLibraryMultiple()
            } else Toast.makeText(context, "Yêu cầu bị từ chối", Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_INTENT_CODE_MULTIPLE -> {
                data?.let { intent ->
                    intent.data?.let {
                        listImages.add(it.toString())
                        updateImageVideo()
                        btnSave.isEnabled = true
                        btnSave.setBackgroundColor(resources.getColor(R.color.colorPost, null))
                    }
                    val clipData: ClipData? = intent.clipData
                    clipData?.let {
                        for (item in 0 until it.itemCount) {
                            listImages.add(it.getItemAt(item).uri.toString())
                        }
                        btnSave.isEnabled = true
                        btnSave.setBackgroundColor(resources.getColor(R.color.colorPost, null))
                        updateImageVideo()
                        Log.e(TAG, "onActivityResult size: ${listImages.size} ")
                    }
                }
            }

            REQUEST_PICK_VIDEO -> {
                data?.let { intent ->
                    videoPath = intent.data.toString()
                    isVideo = true
                    updateImageVideo()
                    btnSave.isEnabled = true
                    btnSave.setBackgroundColor(resources.getColor(R.color.colorPost, null))
                }
            }
        }
    }

    override fun onDestroy() {
        job.cancel()
        super.onDestroy()
    }
}