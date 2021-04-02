package com.caunb163.mxh.ui.main.home.edit_post

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.ClipData
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.caunb163.domain.model.PostEntity
import com.caunb163.mxh.R
import com.caunb163.mxh.state.State
import com.caunb163.mxh.ultis.CheckPermission
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import de.hdodenhof.circleimageview.CircleImageView
import org.koin.android.ext.android.inject
import java.text.SimpleDateFormat
import java.util.*

class EditPostFragment : BottomSheetDialogFragment() {
    private val TAG = "EditPostFragment"

    private val REQUEST_INTENT_CODE_MULTIPLE = 1678

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
    private lateinit var tvImgNumber: TextView
    private lateinit var imvAddImage: ImageView
    private lateinit var progressBar: ProgressBar

    private lateinit var glide: RequestManager
    private val args: EditPostFragmentArgs by navArgs()
    private val viewModel: EditPostViewModel by inject()
    private lateinit var postEntity: PostEntity
    private val listImages = mutableListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_edit_post, container, false)
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
        initView(view)

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
        postEntity = args.post
        fillInformation()
        initObserver()
        addListener()
    }

    private fun initView(view: View) {
        toolbar = view.findViewById(R.id.editpost_toolbar)
        btnSave = view.findViewById(R.id.editpost_btnSave)
        imgAvatar = view.findViewById(R.id.editpost_imv_avatar)
        username = view.findViewById(R.id.editpost_tv_user)
        createDate = view.findViewById(R.id.editpost_tv_date)
        content = view.findViewById(R.id.editpost_edt_content)

        ctl1 = view.findViewById(R.id.editpost_ctl1)
        ctl2 = view.findViewById(R.id.editpost_ctl2)
        ctl3 = view.findViewById(R.id.editpost_ctl3)
        ctl4 = view.findViewById(R.id.editpost_ctl4)
        ctl5 = view.findViewById(R.id.editpost_ctl5)

        img1 = view.findViewById(R.id.editpost_1_img)
        img21 = view.findViewById(R.id.editpost_2_img1)
        img22 = view.findViewById(R.id.editpost_2_img2)
        img31 = view.findViewById(R.id.editpost_3_img1)
        img32 = view.findViewById(R.id.editpost_3_img2)
        img33 = view.findViewById(R.id.editpost_3_img3)
        img41 = view.findViewById(R.id.editpost_4_img1)
        img42 = view.findViewById(R.id.editpost_4_img2)
        img43 = view.findViewById(R.id.editpost_4_img3)
        img44 = view.findViewById(R.id.editpost_4_img4)
        img51 = view.findViewById(R.id.editpost_5_img1)
        img52 = view.findViewById(R.id.editpost_5_img2)
        img53 = view.findViewById(R.id.editpost_5_img3)
        img54 = view.findViewById(R.id.editpost_5_img4)
        img55 = view.findViewById(R.id.editpost_5_img5)
        tvImgNumber = view.findViewById(R.id.editpost_tv_img_number)
        imvAddImage = view.findViewById(R.id.editpost_addimage)
        progressBar = view.findViewById(R.id.editpost_progressbar)
    }

    private fun fillInformation() {
        glide.applyDefaultRequestOptions(RequestOptions()).load(postEntity.userAvatar)
            .into(imgAvatar)
        username.text = postEntity.userName
        createDate.text = getDate(postEntity.createDate)
        content.text = postEntity.content
        listImages.addAll(postEntity.images)
        updateImage()
    }

    private fun getDate(milliSeconds: Long): String? {
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = milliSeconds
        return formatter.format(calendar.time)
    }

    private fun initObserver() {
        viewModel.stateEdit.observe(this, androidx.lifecycle.Observer { state ->
            when (state) {
                is State.Loading -> onLoading()
                is State.Success<*> -> onSuccess()
                is State.Failure -> onFailure(state.message)
            }
        })
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

    private fun addListener() {
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
            val postE = PostEntity(
                postId = postEntity.postId,
                userId = postEntity.userId,
                userName = postEntity.userName,
                userAvatar = postEntity.userAvatar,
                content = content.text.toString(),
                createDate = postEntity.createDate,
                images = listImages,
                arrCmtId = postEntity.arrCmtId,
                arrLike = postEntity.arrLike
            )
            viewModel.editPost(postE)
        }

        imvAddImage.setOnClickListener { ensurePermission() }
    }

    @SuppressLint("SetTextI18n")
    private fun updateImage() {
        when (listImages.size) {
            0 -> {
                showImage(0)
            }

            1 -> {
                showImage(1)
                glide.applyDefaultRequestOptions(RequestOptions())
                    .load(listImages[0])
                    .into(img1)
            }

            2 -> {
                showImage(2)
                glide.applyDefaultRequestOptions(RequestOptions())
                    .load(listImages[0])
                    .into(img21)
                glide.applyDefaultRequestOptions(RequestOptions())
                    .load(listImages[1])
                    .into(img22)
            }

            3 -> {
                showImage(3)
                glide.applyDefaultRequestOptions(RequestOptions())
                    .load(listImages[0])
                    .into(img31)
                glide.applyDefaultRequestOptions(RequestOptions())
                    .load(listImages[1])
                    .into(img32)
                glide.applyDefaultRequestOptions(RequestOptions())
                    .load(listImages[2])
                    .into(img33)
            }

            4 -> {
                showImage(4)
                glide.applyDefaultRequestOptions(RequestOptions())
                    .load(listImages[0])
                    .into(img41)
                glide.applyDefaultRequestOptions(RequestOptions())
                    .load(listImages[1])
                    .into(img42)
                glide.applyDefaultRequestOptions(RequestOptions())
                    .load(listImages[2])
                    .into(img43)
                glide.applyDefaultRequestOptions(RequestOptions())
                    .load(listImages[3])
                    .into(img44)
            }

            else -> {
                showImage(5)
                glide.applyDefaultRequestOptions(RequestOptions())
                    .load(listImages[0])
                    .into(img51)
                glide.applyDefaultRequestOptions(RequestOptions())
                    .load(listImages[1])
                    .into(img52)
                glide.applyDefaultRequestOptions(RequestOptions())
                    .load(listImages[2])
                    .into(img53)
                glide.applyDefaultRequestOptions(RequestOptions())
                    .load(listImages[3])
                    .into(img54)
                glide.applyDefaultRequestOptions(RequestOptions())
                    .load(listImages[4])
                    .into(img55)
                if (listImages.size == 5) {
                    tvImgNumber.visibility = View.INVISIBLE
                } else {
                    tvImgNumber.visibility = View.VISIBLE
                    tvImgNumber.text = "+${listImages.size - 5}"
                }
            }
        }
    }

    private fun showImage(size: Int) {
        ctl1.visibility = View.GONE
        ctl2.visibility = View.GONE
        ctl3.visibility = View.GONE
        ctl4.visibility = View.GONE
        ctl5.visibility = View.GONE

        when (size) {
            1 -> {
                ctl1.visibility = View.VISIBLE
            }
            2 -> {
                ctl2.visibility = View.VISIBLE
            }
            3 -> {
                ctl3.visibility = View.VISIBLE
            }
            4 -> {
                ctl4.visibility = View.VISIBLE
            }
            5 -> {
                ctl5.visibility = View.VISIBLE
            }
            else -> {
            }
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
                        updateImage()
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
                        updateImage()
                        Log.e(TAG, "onActivityResult size: ${listImages.size} ")
                    }
                }
            }
        }
    }
}