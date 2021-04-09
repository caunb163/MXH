package com.caunb163.mxh

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions

class TestActivity : AppCompatActivity() {
    private lateinit var glide: RequestManager
    private lateinit var imageView: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        imageView = findViewById(R.id.image)
        glide = Glide.with(this)
        glide.applyDefaultRequestOptions(
            RequestOptions()
                .placeholder(R.drawable.image_default)
                .error(R.drawable.image_default)
        )

        glide.applyDefaultRequestOptions(RequestOptions()).load(R.raw.qoobee_19).into(imageView)
    }
}