package com.example.dice_game_application

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.PopupWindow;
import android.view.MotionEvent;
import android.widget.Button
import android.widget.ProgressBar


class ChosenPage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chosen_page)

        val mydialog=Dialog(this)
        val about : Button = findViewById(R.id.about)
        val newgame : Button = findViewById(R.id.newgame)

        about.setOnClickListener {
            mydialog.setContentView(R.layout.activity_popup_profile)
            mydialog.show()

        }

        newgame.setOnClickListener {
            val intent = Intent(this, GameScreen::class.java)
            startActivity(intent)
        }

    }
}