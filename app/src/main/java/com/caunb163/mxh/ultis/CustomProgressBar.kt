package com.caunb163.mxh.ultis

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import com.caunb163.mxh.R

class CustomProgressBar(context: Context) : Dialog(context){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)

        setContentView(R.layout.custom_progress_dialog)
        window?.setBackgroundDrawableResource(android.R.color.transparent)
        setCanceledOnTouchOutside(false)
    }

}