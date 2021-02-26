package com.caunb163.mxh.ui.main.home.create_post

import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.caunb163.data.datalocal.LocalStorage
import com.caunb163.domain.model.User
import com.caunb163.mxh.R
import com.caunb163.mxh.state.State
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import de.hdodenhof.circleimageview.CircleImageView
import org.koin.android.ext.android.inject
import java.text.SimpleDateFormat
import java.util.*


class CreatePostFragment : BottomSheetDialogFragment() {
    private val TAG = "CreatePostFragment"
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
            parentLayout?.let { it ->
                val behaviour = BottomSheetBehavior.from(it)
                setupFullHeight(it)
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
    }

    private fun fillInfomation() {
        glide.applyDefaultRequestOptions(RequestOptions()).load(user.photoUrl)
            .into(imgAvatar)
        username.text = user.username
        createDate.text = getDate(timenow, "dd/MM/yyyy")
    }

    fun getDate(milliSeconds: Long, dateFormat: String?): String? {
        val formatter = SimpleDateFormat(dateFormat, Locale.ENGLISH)
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
                    0,
                    false,
                    timenow,
                    listImages,
                    content.text.toString(),
                    0
                )
            }
        }
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

    private fun onLoading() {}

    private fun onSuccess() {
        Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
        dialog?.dismiss()

    }

    private fun onFailure(message: String) {
        Log.e(TAG, "onFailure: $message")
    }
}