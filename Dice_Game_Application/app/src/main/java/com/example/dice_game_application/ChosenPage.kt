package com.example.dice_game_application

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.PopupWindow;
import android.view.MotionEvent;
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible


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
            mydialog.setContentView(R.layout.activity_game_inputs_rules)
            val startButton = mydialog.findViewById(R.id.start_button) as Button
            val target_mark = mydialog.findViewById(R.id.target_field) as EditText
            val user_name = mydialog.findViewById(R.id.name_field) as EditText
            val check_text = mydialog.findViewById(R.id.check) as TextView
            startButton.setOnClickListener {
                if ((target_mark.text.isNotEmpty()) && (user_name.text.isNotEmpty())){
                    val intent = Intent(this, GameScreen::class.java)
                    intent.putExtra("win_mark",target_mark.text.toString().toInt())
                    intent.putExtra("user_name",user_name.text.toString())
                    startActivity(intent)
                }else{
                    check_text.isVisible=true
                }


            }
            mydialog.show()
        }

    }
}