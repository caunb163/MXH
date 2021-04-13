package com.caunb163.mxh.ui.register

import android.annotation.SuppressLint
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.caunb163.mxh.R
import com.caunb163.mxh.base.BaseFragment
import com.caunb163.mxh.state.State
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import org.koin.android.ext.android.inject

class RegisterFragment : BaseFragment(R.layout.fragment_register) {
    private val TAG = "RegisterFragment"

    private lateinit var usernameEdt: TextInputEditText
    private lateinit var emailEdt: TextInputEditText
    private lateinit var passwordEdt: TextInputEditText
    private lateinit var signup: MaterialButton
    private lateinit var signin: TextView
    private lateinit var progressBar: ProgressBar

    private val viewModel: RegisterViewModel by inject()

    @SuppressLint("SetTextI18n")
    override fun initView(view: View) {

        usernameEdt = view.findViewById(R.id.register_edt_name)
        emailEdt = view.findViewById(R.id.register_edt_email)
        passwordEdt = view.findViewById(R.id.register_edt_password)
        signup = view.findViewById(R.id.register_btn_signup)
        signin = view.findViewById(R.id.register_tv_signin)
        progressBar = view.findViewById(R.id.register_progressbar)

        usernameEdt.setText("test")
        passwordEdt.setText("123456")

    }

    override fun initListener() {
        signup.setOnClickListener {
            val username = usernameEdt.text.toString().trim()
            val email = emailEdt.text.toString().trim()
            val password = passwordEdt.text.toString().trim()
            //check empty show error

            viewModel.createAccount(username, email, password)
        }

        signin.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
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
    }

    private fun onLoading() {
        progressBar.visibility = View.VISIBLE
        signup.visibility = View.INVISIBLE
    }

    private fun onSuccess() {
        progressBar.visibility = View.INVISIBLE
        signup.visibility = View.VISIBLE
        Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
        findNavController().navigate(R.id.action_registerFragment_to_loginFragment)

    }

    private fun onFailure(message: String) {
        progressBar.visibility = View.INVISIBLE
        signup.visibility = View.VISIBLE
        showToast(message)
    }
}