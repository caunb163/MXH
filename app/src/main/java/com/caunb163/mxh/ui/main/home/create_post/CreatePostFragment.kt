package com.caunb163.mxh.ui.main.home.create_post

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
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.caunb163.data.datalocal.LocalStorage
import com.caunb163.domain.model.User
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


class CreatePostFragment : BottomSheetDialogFragment() {
    private val TAG = "CreatePostFragment"

    private val REQUEST_INTENT_CODE_MULTIPLE = 5678

    private lateinit var toolbar: Toolbar
    private lateinit var btnpost: Button
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

    private val localStorage: LocalStorage by inject()
    private lateinit var glide: RequestManager
    private lateinit var user: User
    private val viewModel: CreatePostViewModel by inject()
    private val listImages = mutableListOf<String>()
    private var timenow: Long = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_post, container, false)
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
        initview(view)
        timenow = System.currentTimeMillis()
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
        user = localStorage.getAccount()!!
        fillInfomation()
        initObserver()
        addListener()
    }

    private fun initview(view: View) {
        toolbar = view.findViewById(R.id.createpost_toolbar)
        btnpost = view.findViewById(R.id.createpost_btnpost)
        imgAvatar = view.findViewById(R.id.createpost_imv_avatar)
        username = view.findViewById(R.id.createpost_tv_user)
        createDate = view.findViewById(R.id.createpost_tv_date)
        content = view.findViewById(R.id.createpost_edt_content)

        ctl1 = view.findViewById(R.id.createpost_ctl1)
        ctl2 = view.findViewById(R.id.createpost_ctl2)
        ctl3 = view.findViewById(R.id.createpost_ctl3)
        ctl4 = view.findViewById(R.id.createpost_ctl4)
        ctl5 = view.findViewById(R.id.createpost_ctl5)

        img1 = view.findViewById(R.id.createpost_1_img)
        img21 = view.findViewById(R.id.createpost_2_img1)
        img22 = view.findViewById(R.id.createpost_2_img2)
        img31 = view.findViewById(R.id.createpost_3_img1)
        img32 = view.findViewById(R.id.createpost_3_img2)
        img33 = view.findViewById(R.id.createpost_3_img3)
        img41 = view.findViewById(R.id.createpost_4_img1)
        img42 = view.findViewById(R.id.createpost_4_img2)
        img43 = view.findViewById(R.id.createpost_4_img3)
        img44 = view.findViewById(R.id.createpost_4_img4)
        img51 = view.findViewById(R.id.createpost_5_img1)
        img52 = view.findViewById(R.id.createpost_5_img2)
        img53 = view.findViewById(R.id.createpost_5_img3)
        img54 = view.findViewById(R.id.createpost_5_img4)
        img55 = view.findViewById(R.id.createpost_5_img5)
        tvImgNumber = view.findViewById(R.id.createpost_tv_img_number)
        imvAddImage = view.findViewById(R.id.createpost_addimage)
        progressBar = view.findViewById(R.id.createpost_progressbar)
    }

    private fun fillInfomation() {
        glide.applyDefaultRequestOptions(RequestOptions()).load(user.photoUrl)
            .into(imgAvatar)
        username.text = user.username
        createDate.text = getDate(timenow)
    }

    private fun getDate(milliSeconds: Long): String? {
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = milliSeconds
        return formatter.format(calendar.time)
    }

    private fun addListener() {
        content.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                s?.let {
                    if (it.isEmpty()) {
                        btnpost.isEnabled = false
                        btnpost.setBackgroundColor(resources.getColor(R.color.colorUnPost, null))
                    } else {
                        btnpost.isEnabled = true
                        btnpost.setBackgroundColor(resources.getColor(R.color.colorPost, null))
                    }
                }
            }
        })

        btnpost.setOnClickListener {
            if (content.text.isNotEmpty()) {
                viewModel.createPost(
                    user.userId,
                    timenow,
                    listImages,
                    content.text.toString()
                )
            }
        }

        imvAddImage.setOnClickListener {
            ensurePermission()
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

    private fun initObserver() {
        viewModel.state.observe(this, androidx.lifecycle.Observer { state ->
            when (state) {
                is State.Loading -> onLoading()
                is State.Success<*> -> onSuccess()
                is State.Failure -> onFailure(state.message)
            }
        })
    }

    private fun onLoading() {
        btnpost.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
    }

    private fun onSuccess() {
        btnpost.visibility = View.VISIBLE
        progressBar.visibility = View.INVISIBLE
        Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
        dialog?.dismiss()

    }

    private fun onFailure(message: String) {
        btnpost.visibility = View.VISIBLE
        progressBar.visibility = View.INVISIBLE
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
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
                data?.let {
                    it.data?.let {
                        listImages.add(it.toString())
                        updateImage()
                    }
                    val clipData: ClipData? = it.clipData
                    clipData?.let {
                        listImages.clear()
                        for (item in 0 until it.itemCount) {
                            listImages.add(it.getItemAt(item).uri.toString())
                        }
                        updateImage()
                        Log.e(TAG, "onActivityResult size: ${listImages.size} ")
                    }
                }
            }
        }
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

}