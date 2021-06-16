package com.caunb163.mxh.ui.main.profile.updateProfile

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Build
import android.text.TextUtils
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import com.caunb163.data.datalocal.LocalStorage
import com.caunb163.domain.model.User
import com.caunb163.mxh.MainActivity
import com.caunb163.mxh.R
import com.caunb163.mxh.base.BaseDialogFragment
import com.caunb163.mxh.state.State
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.android.material.textfield.TextInputEditText
import org.koin.android.ext.android.inject

class UpdateProfileFragment : BaseDialogFragment() {
    private val TAG = "UpdateProfileFragment"

    private lateinit var toolbar: MaterialToolbar
    private lateinit var btnSave: MaterialButton
    private lateinit var progressBar: ProgressBar
    private lateinit var edtUsername: TextInputEditText
    private lateinit var tvBirthday: TextView
    private lateinit var tvGender: MaterialAutoCompleteTextView
    private lateinit var edtPhone: TextInputEditText
    private lateinit var edtIntro: TextInputEditText
    private val localStorage: LocalStorage by inject()
    private val viewModel: UpdateProfileViewModel by inject()
    private lateinit var user: User

    override fun getLayoutId(): Int = R.layout.fragment_update_profile

    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("SetTextI18n")
    override fun initView(view: View) {
        toolbar = view.findViewById(R.id.updateprofile_toolbar)
        btnSave = view.findViewById(R.id.updateprofile_save)
        progressBar = view.findViewById(R.id.updateprofile_progressbar)
        edtUsername = view.findViewById(R.id.updateprofile_username)
        tvBirthday = view.findViewById(R.id.updateprofile_birthday)
        tvGender = view.findViewById(R.id.updateprofile_gender)
        edtPhone = view.findViewById(R.id.updateprofile_phone)
        edtIntro = view.findViewById(R.id.updateprofile_intro)

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        toolbar.setNavigationOnClickListener {
            (activity as MainActivity).onBackPressed()
        }

        val items = resources.getStringArray(R.array.gender)
        val adapter = ArrayAdapter(requireContext(), R.layout.exposed_dropdown_menu, items)
        tvGender.setAdapter(adapter)
        tvGender.setOnClickListener { tvGender.error = null }

        user = localStorage.getAccount()!!
        edtUsername.setText(user.username)
        tvBirthday.text = user.birthDay

        tvBirthday.setOnClickListener {
            tvBirthday.error = null
            val c = user.birthDay
            val currentTime = Calendar.getInstance()
            var mYear = currentTime[Calendar.YEAR]
            var mMonth = currentTime[Calendar.MONTH]
            var mDay = currentTime[Calendar.DAY_OF_MONTH]
            if (c.isNotEmpty()) {
                val arr = c.split("/")
                mYear = arr[2].toInt()
                mMonth = arr[1].toInt() - 1
                mDay = arr[0].toInt()
            }
            val dpd = DatePickerDialog(
                requireContext(),
                DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                    tvBirthday.text =
                        "${"%02d".format(dayOfMonth)}/${"%02d".format(monthOfYear + 1)}/${
                            "%04d".format(
                                year
                            )
                        }"
                },
                mYear,
                mMonth,
                mDay
            )
            dpd.show()
        }

        when (user.gender) {
            "Nam" -> tvGender.setText("Nam", false)
            "Nữ" -> tvGender.setText("Nữ", false)
            "Khác" -> tvGender.setText("Khác", false)
        }
        edtPhone.setText(user.phone)
        edtIntro.setText(user.intro)
    }

    override fun initListener() {
        btnSave.setOnClickListener {
            val text1 = edtUsername.text.toString().trim()
            if (text1.isNotEmpty()) {
                val text2 = tvBirthday.text.toString().trim()
                if (!TextUtils.equals(text2, "Ngày sinh") && text2.isNotEmpty()) {
                    val text3 = tvGender.editableText.toString().trim()
                    if (text3.isNotEmpty()) {
                        user.username = text1
                        user.birthDay = text2
                        user.gender = text3
                        user.intro = edtIntro.text.toString().trim()
                        user.phone = edtPhone.text.toString().trim()
                        viewModel.uploadProfile(user)
                    } else {
                        tvGender.error = "Vui lòng chọn giới tính"
                    }
                } else {
                    tvBirthday.error = "Vui lòng chọn ngày sinh"
                }
            } else {
                edtUsername.error = "Vui lòng điền tên hiển thị"
            }
        }
    }

    override fun initObserve() {
        viewModel.stateUpdate.observe(this, Observer { state ->
            when (state) {
                is State.Loading -> onLoading()
                is State.Success<*> -> onSuccess()
                is State.Failure -> onFailure(state.message)
            }
        })
    }

    private fun onLoading(){
        btnSave.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
    }

    private fun onSuccess(){
        btnSave.visibility = View.VISIBLE
        progressBar.visibility = View.GONE
        dismiss()
    }

    private fun onFailure(message: String){
        btnSave.visibility = View.VISIBLE
        progressBar.visibility = View.GONE
        showToast(message)
    }

}