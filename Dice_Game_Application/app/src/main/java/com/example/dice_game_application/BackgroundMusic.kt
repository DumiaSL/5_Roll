package com.example.dice_game_application

import android.content.Context
import android.media.MediaPlayer

class BackgroundMusic private constructor(context: Context) {
    companion object {
        @Volatile
        private var INSTANCE: BackgroundMusic? = null

        fun getInstance(context: Context): BackgroundMusic =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: BackgroundMusic(context).also { INSTANCE = it }
            }
    }

    private val mediaPlayer = MediaPlayer.create(context, R.raw.background_track)

    init {
        mediaPlayer.isLooping = true
    }

    fun start() {
        mediaPlayer.start()
    }

    fun stop() {
        mediaPlayer.pause()
        mediaPlayer.seekTo(0)
    }
}
