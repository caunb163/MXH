package com.caunb163.mxh.ui.main.home.view_image

import android.content.Context
import android.media.MediaParser
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.MediaController
import android.widget.ProgressBar
import android.widget.VideoView
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.caunb163.mxh.R
import com.caunb163.mxh.base.BaseFragment
import com.github.chrisbanes.photoview.PhotoView
//import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.PlayerView

class ImageViewPageFragment(
    private val position: Int,
    private val list: MutableList<String>,
    private val glide: RequestManager,
    private val boolean: Boolean,
    private val mContext: Context
) : BaseFragment(R.layout.fragment_image_view_page) {
    private lateinit var imageView: PhotoView
    private lateinit var videoView: VideoView
    private lateinit var videoContainer: FrameLayout
    private lateinit var progressBar: ProgressBar
//    private var player: SimpleExoPlayer? = null
//    private var playWhenReady = true
//    private var currentWindow: Int = 0
//    private var playbackPosition: Long = 0

    private var playbackPosition = 0
    private lateinit var mediaController: MediaController

    override fun initView(view: View) {
        videoContainer = view.findViewById(R.id.videoview_container)
        progressBar = view.findViewById(R.id.videoview_progressbar)
        imageView = view.findViewById(R.id.imageview_view)
        videoView = view.findViewById(R.id.videoview_view)
        videoView.visibility = View.GONE
        imageView.visibility = View.GONE

        mediaController = MediaController(requireContext())
        videoView.setOnPreparedListener { mp ->
            mediaController.setAnchorView(videoView)
            mp.isLooping = true
            videoView.setMediaController(mediaController)
            videoView.seekTo(playbackPosition)
            videoView.start()
        }

        videoView.setOnInfoListener { mp, what, extra ->
            if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
                progressBar.visibility = View.INVISIBLE
            }
            true
        }
    }

    override fun onStart() {
        super.onStart()
        if (boolean) {
            if (position == 0) {
                videoView.visibility = View.VISIBLE
                val uri = Uri.parse(list[position])
                videoView.setVideoURI(uri)
                progressBar.visibility = View.VISIBLE
//                player = SimpleExoPlayer.Builder(mContext).build()
//                videoView.player = player
////                val mediaItem = MediaItem.fromUri(list[position])
////                player?.setMediaItem(mediaItem)
//                player?.playWhenReady = playWhenReady
//                player?.seekTo(currentWindow, playbackPosition)
//                player?.prepare()
            } else {
                imageView.visibility = View.VISIBLE
                glide.applyDefaultRequestOptions(RequestOptions()).load(list[position])
                    .into(imageView)
            }
        } else {
            imageView.visibility = View.VISIBLE
            glide.applyDefaultRequestOptions(RequestOptions()).load(list[position]).into(imageView)
        }
    }
//
//    override fun onResume() {
//        super.onResume()
//        videoView.setOnPreparedListener { mp ->
//            if (!mp.isPlaying) {
//                videoView.resume()
//            }
//        }
//    }

    override fun initListener() {}
    override fun initObserve() {}

    override fun onPause() {
        super.onPause()
        videoView.pause()
        playbackPosition = videoView.currentPosition
//        if (player != null) {
//            player!!.pause()
//        }
    }

    override fun onStop() {
        videoView.stopPlayback()
        super.onStop()
    }

//    override fun onDestroy() {
//        super.onDestroy()
////        videoView.stopPlayback()
////        if (player != null) {
////            playbackPosition = player!!.currentPosition
////            currentWindow = player!!.currentWindowIndex
////            playWhenReady = player!!.playWhenReady
////            player!!.release()
////            player = null
////        }
//    }

}