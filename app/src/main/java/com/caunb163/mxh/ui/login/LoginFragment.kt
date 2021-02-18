package com.caunb163.mxh.ui.login

import android.annotation.SuppressLint
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.caunb163.domain.model.User
import com.caunb163.mxh.R
import com.caunb163.mxh.base.BaseFragment
import com.caunb163.mxh.state.State
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import eightbitlab.com.blurview.BlurView
import eightbitlab.com.blurview.RenderScriptBlur
import org.koin.android.ext.android.inject

class LoginFragment : BaseFragment(R.layout.fragment_login) {
    private val TAG = "LoginFragment"

    private lateinit var blurView: BlurView
    private lateinit var emailEdt: TextInputEditText
    private lateinit var passwordEdt: TextInputEditText
    private lateinit var forgotPassword: TextView
    private lateinit var signin: MaterialButton
    private lateinit var signup: TextView

    private val viewModel: LoginViewModel by inject()

    @SuppressLint("SetTextI18n")
    override fun initView(view: View) {
        blurView = view.findViewById(R.id.login_blurview)
        blurviewBackground()

        emailEdt = view.findViewById(R.id.login_edt_email)
        passwordEdt = view.findViewById(R.id.login_edt_password)
        forgotPassword = view.findViewById(R.id.login_tv_forgot_password)
        signin = view.findViewById(R.id.login_btn_signin)
        signup = view.findViewById(R.id.login_tv_signup)

        emailEdt.setText("test@gmail.com")
        passwordEdt.setText("123456")
    }

    override fun initListener() {
        signin.setOnClickListener {
            val email = emailEdt.text.toString().trim()
            val password = passwordEdt.text.toString().trim()
            //check empty
            viewModel.loginWithEmailAndPassword(email, password)
        }

    }

    override fun initObserve() {
        viewModel.state.observe(this, Observer { state ->
            when (state) {
                is State.Loading -> onLoading()
                is State.Success<*> -> onSuccess(state.data as User)
                is State.Failure -> onFailure(state.message)
            }
        })
    }

    private fun onLoading() {}

    private fun onSuccess(user: User) {
        Log.e(TAG, "onSuccess: ${user.toString()}")
    }

    private fun onFailure(message: String) {
        Log.e(TAG, "onFailure: $message")
    }

    private fun blurviewBackground() {
        val radius: Float = 0.01f
        val decorView: View = requireActivity().window.decorView
        val rootView = decorView.findViewById<View>(android.R.id.content) as ViewGroup
        val windowBackground = decorView.background
        blurView.setupWith(rootView)
            .setFrameClearDrawable(windowBackground)
            .setBlurAlgorithm(RenderScriptBlur(context))
            .setBlurRadius(radius)
            .setBlurAutoUpdate(true)
            .setHasFixedTransformationMatrix(true)
    }

//    private fun checkEmpty(text: String){
//        if (text.isEmpty()){
//
//        }
//    }

}