package com.caunb163.mxh.ui.main.video

import android.content.Context
import com.google.android.exoplayer2.DefaultLoadControl
import com.google.android.exoplayer2.DefaultRenderersFactory
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.util.Clock
import kohii.v1.exoplayer.ExoPlayerConfig
import kohii.v1.exoplayer.Kohii
import kohii.v1.exoplayer.createKohii
import kohii.v1.utils.Capsule

object KohiiProvider {


    private val capsule = Capsule<Kohii, Context>(creator = { context ->
        createKohii(
            context.applicationContext, ExoPlayerConfig.FAST_START
        )
    })

    fun get(context: Context) = capsule.get(context)
}
