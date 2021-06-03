package com.caunb163.mxh.ui.main.home.view_image

//import com.google.android.exoplayer2.MediaItem
import android.content.Context
import android.net.Uri
import android.view.View
import android.widget.FrameLayout
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.caunb163.mxh.R
import com.caunb163.mxh.base.BaseFragment
import com.github.chrisbanes.photoview.PhotoView
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory

class ImageViewPageFragment(
    private val position: Int,
    private val list: MutableList<String>,
    private val glide: RequestManager,
    private val boolean: Boolean,
    private val mContext: Context
) : BaseFragment(R.layout.fragment_image_view_page) {
    private lateinit var imageView: PhotoView
    private lateinit var videoView: PlayerView
    private lateinit var videoContainer: FrameLayout

    private var playWhenReady = true
    private var currentWindow: Int = 0
    private var playbackPosition: Long = 0

    private var player: SimpleExoPlayer? = null
    private val dataSourceFactory: DataSource.Factory by lazy {
        DefaultDataSourceFactory(mContext, "exoplayer-sample")
    }

    override fun initView(view: View) {
        videoContainer = view.findViewById(R.id.videoview_container)
        imageView = view.findViewById(R.id.imageview_view)
        videoView = view.findViewById(R.id.videoview_view)
        videoView.visibility = View.GONE
        imageView.visibility = View.GONE

    }

    override fun onStart() {
        super.onStart()
        if (boolean) {
            if (position == 0) {
                videoView.visibility = View.VISIBLE
                player = SimpleExoPlayer.Builder(mContext).build()
                videoView.player = player
                player?.playWhenReady = playWhenReady
                player?.repeatMode = Player.REPEAT_MODE_ONE
                player?.seekTo(currentWindow, playbackPosition)
                player?.prepare(
                    ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(
                        Uri.parse(list[position])
                    )
                )
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

    override fun onResume() {
        super.onResume()
        if (player != null) {
            player!!.playWhenReady = true
            player!!.playbackState
        }
    }

    override fun onPause() {
        if (player != null) {
            player!!.playWhenReady = false
            player!!.playbackState
        }
        super.onPause()
    }

    override fun onDestroy() {
        releasePlayer()
        super.onDestroy()
    }

    private fun releasePlayer() {
        if (player != null) {
            playWhenReady = player!!.playWhenReady
            playbackPosition = player!!.currentPosition
            currentWindow = player!!.currentWindowIndex
            player!!.release()
            player = null
        }
    }

}