package com.example.dice_game_application

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import android.widget.Toast


class BackgroundSoundService : Service() {
    var mediaPlayer: MediaPlayer? = null
    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        mediaPlayer = MediaPlayer.create(this, R.raw.background_track)
        mediaPlayer!!.isLooping = true // Set looping
        mediaPlayer!!.setVolume(30f, 30f)
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        mediaPlayer!!.start()
        return startId
    }

    override fun onStart(intent: Intent, startId: Int) {}
    override fun onDestroy() {
        mediaPlayer!!.pause()
    }

    fun pause() {
        mediaPlayer?.pause()
    }

    fun resume() {
        mediaPlayer?.start()
    }

    override fun onLowMemory() {}
}
