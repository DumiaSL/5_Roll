package com.example.dice_game_application

import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import android.widget.Button
import android.widget.ProgressBar

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Progress bar loading 2s
        //source - https://youtu.be/xU-Cc41DfTg
        val progressBar : ProgressBar = findViewById(R.id.progressBar)

        progressBar.max=100

        val currentProgress=100

        ObjectAnimator.ofInt(progressBar,"progress",currentProgress)
            .setDuration(2000)
            .start()


        //Move to next page in 3s
        //source - https://www.geeksforgeeks.org/how-to-create-a-splash-screen-in-android-using-kotlin/
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, ChosenPage::class.java)
            startActivity(intent)
            finish()
        }, 2500)

    }
}