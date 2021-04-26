package com.caunb163.mxh.ui.main.home.view_image

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.caunb163.mxh.R
import com.caunb163.mxh.base.BaseFragment
import com.github.chrisbanes.photoview.PhotoView
import com.google.android.exoplayer2.MediaItem
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
    private lateinit var videoView: PlayerView
    private var player: SimpleExoPlayer? = null
    private var playWhenReady = true
    private var currentWindow: Int = 0
    private var playbackPosition: Long = 0

    override fun initView(view: View) {
        imageView = view.findViewById(R.id.imageview_view)
        videoView = view.findViewById(R.id.videoview_view)
        videoView.visibility = View.GONE
        imageView.visibility = View.GONE
        if (boolean) {
            if (position == 0) {
                videoView.visibility = View.VISIBLE
                player = SimpleExoPlayer.Builder(mContext).build()
                videoView.player = player

                val mediaItem = MediaItem.fromUri(list[position])
                player?.setMediaItem(mediaItem)
                player?.playWhenReady = playWhenReady
                player?.seekTo(currentWindow, playbackPosition)
                player?.prepare()
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

    override fun initListener() {}
    override fun initObserve() {}

    override fun onDestroy() {
        super.onDestroy()
        if (player != null) {
            playbackPosition = player!!.currentPosition
            currentWindow = player!!.currentWindowIndex
            playWhenReady = player!!.playWhenReady
            player!!.release()
            player = null
        }
    }

}