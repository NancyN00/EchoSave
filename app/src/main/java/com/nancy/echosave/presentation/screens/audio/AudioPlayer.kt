package com.nancy.echosave.presentation.screens.audio

import android.content.Context
import androidx.core.net.toUri
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import java.io.File
import androidx.media3.common.Player

class AudioPlayer(context: Context) {
    val player = ExoPlayer.Builder(context.applicationContext).build()

    fun play(file: File) {
        val mediaItem = MediaItem.fromUri(file.toUri())
        player.setMediaItem(mediaItem)
        player.prepare()
        player.play()
    }

    fun togglePlayPause() {
        if (player.isPlaying) player.pause() else player.play()
    }

    fun release() {
        player.release()
    }
}

