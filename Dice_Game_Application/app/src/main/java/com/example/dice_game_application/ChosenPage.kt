package com.example.dice_game_application

import android.app.AlertDialog
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

        var computerWins=intent.getIntExtra("ComputerWins",0)
        var userWins=intent.getIntExtra("UserWins",0)


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
                    intent.putExtra("ComputerWins",computerWins)
                    intent.putExtra("UserWins",userWins)
                    startActivity(intent)
                }else{
                    check_text.isVisible=true
                }
            }
            mydialog.show()
        }
    }
    override fun onBackPressed() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Are you sure you want to exit?")
        builder.setCancelable(false)
        builder.setPositiveButton("Yes") { dialog, which ->
            finish()
        }
        builder.setNegativeButton("No") { dialog, which ->
            // Do nothing
        }
        val dialog = builder.create()
        dialog.show()
    }
}