package com.caunb163.mxh.ui.login.phone

import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ViewFlipper
import com.caunb163.mxh.R
import com.caunb163.mxh.base.BaseFragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import eightbitlab.com.blurview.BlurView
import eightbitlab.com.blurview.RenderScriptBlur
import java.util.concurrent.TimeUnit

class PhoneFragment : BaseFragment(R.layout.fragment_phone) {
    private val TAG = "PhoneFragment"

    private lateinit var blurView: BlurView
    private lateinit var phoneEdt: TextInputEditText
    private lateinit var verifyEdt: TextInputEditText
    private lateinit var signin: MaterialButton
    private lateinit var verifyBtn: MaterialButton
    private lateinit var viewFlipper: ViewFlipper
    private lateinit var mToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var mVerificationId: String

    private lateinit var auth: FirebaseAuth
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks

    private var phone = "+84399138877"
    override fun initView(view: View) {
        blurView = view.findViewById(R.id.phone_blurview)
        blurviewBackground()

        phoneEdt = view.findViewById(R.id.phone_edt)
        verifyEdt = view.findViewById(R.id.phone_verifyCode)
        signin = view.findViewById(R.id.phone_signin)
        verifyBtn = view.findViewById(R.id.phone_verifyBtn)
        viewFlipper = view.findViewById(R.id.phone_viewflipper)

        auth = Firebase.auth
        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                Log.e(TAG, "onVerificationCompleted:$credential")
//                signInWithPhoneAuthCredential(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Log.e(TAG, "onVerificationFailed", e)

                if (e is FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                } else if (e is FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                }

            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                Log.e(TAG, "onCodeSent:$verificationId")

                mVerificationId = verificationId
                mToken = token
            }
        }
    }

    override fun initListener() {

        signin.setOnClickListener {
            phoneEdt.text?.let {
                startPhoneNumberVerify()
                viewFlipper.displayedChild = 1
            }
        }

        verifyBtn.setOnClickListener {
            verifyEdt.text?.let {
                val credential = PhoneAuthProvider.getCredential(mVerificationId, it.toString().trim())
                signInWithPhoneAuthCredential(credential)
            }
        }
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

    private fun resendVerificationCode(
        phone: String,
        token: PhoneAuthProvider.ForceResendingToken
    ) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phone)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(requireActivity())
            .setCallbacks(callbacks)
            .setForceResendingToken(token)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    override fun initObserve() {}

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

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.e(TAG, "signInWithCredential:success")
                    val user = task.result?.user
                    Log.e(TAG, "signInWithPhoneAuthCredential: ${user?.uid}")
                    Log.e(TAG, "signInWithPhoneAuthCredential: ${user?.phoneNumber}")
                } else {
                    // Sign in failed, display a message and update the UI
                    Log.e(TAG, "signInWithCredential:failure", task.exception)
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                    }
                    // Update UI
                }
            }
    }
}