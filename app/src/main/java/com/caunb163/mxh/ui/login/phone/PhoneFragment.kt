package com.caunb163.mxh.ui.login.phone

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.*
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.caunb163.mxh.R
import com.caunb163.mxh.base.BaseFragment
import com.caunb163.mxh.state.State
import com.chaos.view.PinView
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.hbb20.CountryCodePicker
import org.koin.android.ext.android.inject
import java.util.*
import java.util.concurrent.TimeUnit

class PhoneFragment : BaseFragment(R.layout.fragment_phone) {
    private val TAG = "PhoneFragment"

    private lateinit var countryCodePicker: CountryCodePicker
    private lateinit var phoneEdt: EditText
    private lateinit var btnContinue: MaterialButton
    private lateinit var btnVerify: MaterialButton
    private lateinit var btnSignUp: MaterialButton
    private lateinit var pinView: PinView
    private lateinit var progressBar1: ProgressBar
    private lateinit var progressBar2: ProgressBar
    private lateinit var viewFlipper: ViewFlipper
    private lateinit var btnSignin: TextView
    private lateinit var btnResend: TextView
    private lateinit var birthday: TextView
    private lateinit var username: TextInputEditText
    private lateinit var gender: MaterialAutoCompleteTextView

    private lateinit var mToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var mVerificationId: String
    private lateinit var auth: FirebaseAuth
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks

    private val viewModel: PhoneViewModel by inject()
    private var phone = ""

    override fun initView(view: View) {

        countryCodePicker = view.findViewById(R.id.loginPhone_ccp)
        phoneEdt = view.findViewById(R.id.loginPhone_edt_number)
        btnContinue = view.findViewById(R.id.loginPhone_btn_continue)
        pinView = view.findViewById(R.id.loginPhone_pinView)
        btnVerify = view.findViewById(R.id.loginPhone_btn_verify)
        progressBar1 = view.findViewById(R.id.loginPhone_progressbar1)
        progressBar2 = view.findViewById(R.id.loginPhone_progressbar2)
        btnSignin = view.findViewById(R.id.loginPhone_tv_signin)
        viewFlipper = view.findViewById(R.id.loginPhone_viewFlipper)
        btnResend = view.findViewById(R.id.loginPhone_resendCode)
        username = view.findViewById(R.id.loginPhone_username)
        birthday = view.findViewById(R.id.loginPhone_birthDay)
        gender = view.findViewById(R.id.loginPhone_gender)
        btnSignUp = view.findViewById(R.id.loginPhone_btn_signup)

        val items = resources.getStringArray(R.array.gender)
        val adapter = ArrayAdapter(requireContext(), R.layout.exposed_dropdown_menu, items)
        gender.setAdapter(adapter)

        auth = Firebase.auth
        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {}

            override fun onVerificationFailed(e: FirebaseException) {
                showToast("${e.message}")
                Log.e(TAG, "onVerificationFailed: ${e.message}")
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                Log.e(TAG, "onCodeSent: $verificationId ---- $token")
                mVerificationId = verificationId
                mToken = token
            }
        }

    }

    @SuppressLint("SetTextI18n", "ResourceAsColor")
    override fun initListener() {
        btnContinue.setOnClickListener {
            var text = phoneEdt.text.toString().trim()
            if (text.isNotEmpty()) {
                if (text.length in 9..10) {
                    if (text.startsWith("0")) {
                        text = text.substring(1)
                        phone = "+${countryCodePicker.selectedCountryCode}$text"
                        startPhoneNumberVerify()
                        viewFlipper.displayedChild = 1
                    } else {
                        phone = "+${countryCodePicker.selectedCountryCode}$text"
                        startPhoneNumberVerify()
                        viewFlipper.displayedChild = 1
                    }
                } else {
                    phoneEdt.error = "Số điện thoại không hợp lệ"
                }
            } else {
                phoneEdt.error = "Vui lòng điền số điện thoại"
            }
        }

        btnVerify.setOnClickListener {
            hideKeyboardFrom(requireContext(), it)
            val text = pinView.text.toString()
            if (text.length != 6) {
                pinView.error = "Vui lòng điền đầy đủ"
            } else {
                viewModel.checkPhone(mVerificationId, text)
            }
        }

        btnSignin.setOnClickListener {
            findNavController().navigate(R.id.action_phoneFragment_to_loginFragment)
        }

        btnResend.setOnClickListener {
            val options = PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber(phone)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(requireActivity())
                .setCallbacks(callbacks)
                .setForceResendingToken(mToken)
                .build()
            PhoneAuthProvider.verifyPhoneNumber(options)
            showToast("Resend")
        }

        birthday.setOnClickListener {
            hideKeyboardFrom(requireContext(), username)
            birthday.error = null
            val c = Calendar.getInstance()
            val mYear = c.get(Calendar.YEAR)
            val mMonth = c.get(Calendar.MONTH)
            val mDay = c.get(Calendar.DAY_OF_MONTH)

            val dpd = DatePickerDialog(
                requireContext(),
                DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                    birthday.text =
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

        gender.setOnClickListener { gender.error = null }

        btnSignUp.setOnClickListener {
            hideKeyboardFrom(requireContext(), username)
            val text1 = username.text.toString().trim()
            if (text1.isNotEmpty()) {
                val text2 = birthday.text.toString().trim()
                if (!TextUtils.equals(text2, "Ngày sinh") && text2.isNotEmpty()) {
                    val text3 = gender.editableText.toString().trim()
                    if (text3.isNotEmpty()) {
                        viewModel.createUser(text1, text2, text3)
                    } else {
                        gender.error = "Vui lòng chọn giới tính"
                    }
                } else {
                    birthday.error = "Vui lòng chọn ngày sinh"
                }
            } else {
                username.error = "Vui lòng điền tên hiển thị"
            }
        }
    }

    override fun initObserve() {
        viewModel.stateUser.observe(this, Observer { state ->
            when (state) {
                is State.Loading -> onLoading()
                is State.Success<*> -> onSuccess()
                is State.Failure -> onFailure(state.message)
            }
        })

        viewModel.stateCheck.observe(this, Observer { state ->
            when (state) {
                is State.Loading -> onLoading()
                is State.Success<*> -> onSuccess(state.data as Boolean)
                is State.Failure -> onFailure(state.message)
            }
        })

        viewModel.stateCreateUser.observe(this, Observer { state ->
            when (state) {
                is State.Loading -> onLoadingCreate()
                is State.Success<*> -> onSuccessCreate()
                is State.Failure -> onFailure(state.message)
            }
        })
    }

    private fun onLoading() {
        btnVerify.visibility = View.INVISIBLE
        progressBar1.visibility = View.VISIBLE
    }

    private fun onLoadingCreate() {
        btnSignUp.visibility = View.INVISIBLE
        progressBar2.visibility = View.VISIBLE
    }

    private fun onSuccessCreate() {
        btnSignUp.visibility = View.VISIBLE
        progressBar2.visibility = View.INVISIBLE
        findNavController().navigate(R.id.action_phoneFragment_to_mainFragment)
    }

    private fun onSuccess() {
        btnVerify.visibility = View.VISIBLE
        progressBar1.visibility = View.INVISIBLE
        findNavController().navigate(R.id.action_phoneFragment_to_mainFragment)
    }

    private fun onSuccess(boolean: Boolean) {
        if (boolean) {
            viewModel.getUserPhone()
        } else {
            btnVerify.visibility = View.VISIBLE
            progressBar1.visibility = View.INVISIBLE
            viewFlipper.displayedChild = 2
        }
    }

    private fun onFailure(message: String) {
        btnVerify.visibility = View.VISIBLE
        progressBar1.visibility = View.INVISIBLE
        btnSignUp.visibility = View.VISIBLE
        progressBar2.visibility = View.INVISIBLE
        showToast(message)
    }

    private fun startPhoneNumberVerify() {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phone)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(requireActivity())
            .setCallbacks(callbacks)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }
}