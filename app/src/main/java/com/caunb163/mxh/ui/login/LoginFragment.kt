package com.caunb163.mxh.ui.login

import android.annotation.SuppressLint
import android.content.Intent
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.caunb163.mxh.R
import com.caunb163.mxh.base.BaseFragment
import com.caunb163.mxh.state.State
import com.caunb163.mxh.ultis.CustomProgressBar
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import org.koin.android.ext.android.inject

class LoginFragment : BaseFragment(R.layout.fragment_login) {
    private val TAG = "LoginFragment"
    private val RC_SIGN_IN = 1227

    private lateinit var emailEdt: TextInputEditText
    private lateinit var passwordEdt: TextInputEditText
    private lateinit var forgotPassword: TextView
    private lateinit var signin: MaterialButton
    private lateinit var signup: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var btnPhone: ImageView
    private lateinit var btnGoogle: ImageView
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var customProgressBar: CustomProgressBar

    private val viewModel: LoginViewModel by inject()

    @SuppressLint("SetTextI18n")
    override fun initView(view: View) {

        emailEdt = view.findViewById(R.id.login_edt_email)
        passwordEdt = view.findViewById(R.id.login_edt_password)
        forgotPassword = view.findViewById(R.id.login_tv_forgot_password)
        signin = view.findViewById(R.id.login_btn_signin)
        signup = view.findViewById(R.id.login_tv_signup)
        progressBar = view.findViewById(R.id.login_progressbar)
        btnPhone = view.findViewById(R.id.login_phone)
        btnGoogle = view.findViewById(R.id.login_google)

        customProgressBar = CustomProgressBar(requireContext())
//        emailEdt.setText("test@gmail.com")
//        passwordEdt.setText("123456")

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
    }

    override fun initListener() {
        signin.setOnClickListener {
            val email = emailEdt.text.toString().trim()
            val password = passwordEdt.text.toString().trim()
            //check empty
            if (email.isNotEmpty()) {
                if (password.isNotEmpty()) {
                    viewModel.loginWithEmailAndPassword(email, password)
                } else {
                    passwordEdt.error = "Vui lòng điền mật khẩu"
                }
            } else {
                emailEdt.error = "Vui lòng điền thông tin"
            }
        }

        signup.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        btnGoogle.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }

        btnPhone.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_phoneFragment)
        }

    }

    override fun initObserve() {
        viewModel.state.observe(this, Observer { state ->
            when (state) {
                is State.Loading -> onLoading()
                is State.Success<*> -> onSuccess()
                is State.Failure -> onFailure(state.message)
            }
        })

        viewModel.stateGoogle.observe(this, Observer { state ->
            when (state) {
                is State.Loading -> onLoadingGoogle()
                is State.Success<*> -> onSuccessGoogle()
                is State.Failure -> onFailure(state.message)
            }
        })
    }

    private fun onLoading() {
        progressBar.visibility = View.VISIBLE
        signin.visibility = View.INVISIBLE
    }

    private fun onSuccess() {
        progressBar.visibility = View.INVISIBLE
        signin.visibility = View.VISIBLE
        findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
    }

    private fun onLoadingGoogle() {
        customProgressBar.show()
    }

    private fun onSuccessGoogle() {
        customProgressBar.hide()
        findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
    }

    private fun onFailure(message: String) {
        customProgressBar.hide()
        progressBar.visibility = View.INVISIBLE
        signin.visibility = View.VISIBLE
        showToast(message)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                viewModel.loginWithGoogle(account.idToken!!)
            } catch (e: Exception) {
                showToast("${e.message}")
            }
        }
    }
}