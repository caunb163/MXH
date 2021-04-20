package com.caunb163.mxh

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.MediaController
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity


class TestActivity : AppCompatActivity() {
    private val TAG = "TestActivity"

    private lateinit var videoView: VideoView
    private lateinit var playPause: ImageView
    private var boolean = true
    private var hide = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        videoView = findViewById(R.id.videoView)
        playPause = findViewById(R.id.play_pause)

        val stringPath =
            "https://www.learningcontainer.com/wp-content/uploads/2020/05/sample-mp4-file.mp4"
        videoView.setVideoPath(stringPath)

        playPause.setOnClickListener {
            if (boolean) {
                videoView.start()
                playPause.setImageResource(R.drawable.ic_pause)
                boolean = false
            } else {
                videoView.pause()
                playPause.setImageResource(R.drawable.ic_play)
                boolean = true
            }
        }

        videoView.setOnClickListener {
            if (hide) {
                playPause.visibility = View.INVISIBLE
                hide = false
            } else {
                playPause.visibility = View.VISIBLE
                hide = true
            }
        }
    }

}