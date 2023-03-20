package com.example.dice_game_application

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.core.view.isVisible


class ChosenPage : AppCompatActivity() {
    var computerWins=0
    var userWins=0
    var mydialog: Dialog? =null
    var isNewgamePopup=false
    var isAboutPopup=false
    var level = false
    lateinit var ishardswich: Switch
//    lateinit var user_nameField: EditText
//    lateinit var target_markField: EditText
    var userName: String? =null
    var targetMark=0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chosen_page)

        mydialog=Dialog(this)
        val about : Button = findViewById(R.id.about)
        val newgame : Button = findViewById(R.id.newgame)

        computerWins=intent.getIntExtra("ComputerWins",0)
        userWins=intent.getIntExtra("UserWins",0)

        if (savedInstanceState != null) {
            computerWins = savedInstanceState.getInt("computerWins")
            userWins = savedInstanceState.getInt("userWins")
            isNewgamePopup = savedInstanceState.getBoolean("isNewgamePopup")
            isAboutPopup = savedInstanceState.getBoolean("isAboutPopup")
            level = savedInstanceState.getBoolean("level")
        }

        about.setOnClickListener {
            isAboutPopup=true
            mydialog!!.setContentView(R.layout.activity_popup_profile)
            mydialog!!.setOnCancelListener{
                isAboutPopup=false
            }
            mydialog!!.show()
        }

        newgame.setOnClickListener {
            mydialog!!.setContentView(R.layout.activity_game_inputs_rules)
            isNewgamePopup=true
            val startButton = mydialog!!.findViewById(R.id.start_button) as Button
            var target_markField = mydialog!!.findViewById(R.id.target_field) as EditText
            var user_nameField = mydialog!!.findViewById(R.id.name_field) as EditText
            val check_text = mydialog!!.findViewById(R.id.check) as TextView
            ishardswich = mydialog!!.findViewById(R.id.hardswich) as Switch

            ishardswich?.setOnCheckedChangeListener { _, isChecked ->
                level = isChecked
            }

            //
            user_nameField.setText("User")
            mydialog!!.setOnCancelListener{
                isNewgamePopup=false
            }

            target_markField.setText("101")
            startButton.setOnClickListener {
                if ((target_markField.text.isNotEmpty()) && (user_nameField.text.isNotEmpty())){
                    userName=user_nameField.text.toString()
                    targetMark= target_markField.text.toString().toInt()

                    val intent = Intent(this, GameScreen::class.java)
                    intent.putExtra("win_mark",targetMark)
                    intent.putExtra("user_name",userName)
                    intent.putExtra("ComputerWins",computerWins)
                    intent.putExtra("UserWins",userWins)
                    intent.putExtra("level",level)
                    startActivity(intent)
                    mydialog!!.dismiss()
                }else{
                    check_text.isVisible=true
                }
            }
            mydialog!!.show()
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("computerWins", computerWins)
        outState.putInt("userWins", userWins)
        outState.putBoolean("isNewgamePopup", isNewgamePopup)
        outState.putBoolean("isAboutPopup", isAboutPopup)
        outState.putBoolean("level", level)

//        userName= user_nameField.text.toString()
//        targetMark= target_markField.text.toString().toInt()
        outState.putInt("targetMark", targetMark)
        outState.putString("userName", userName)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        computerWins = savedInstanceState.getInt("computerWins")
        userWins = savedInstanceState.getInt("userWins")
        isNewgamePopup = savedInstanceState.getBoolean("isNewgamePopup")
        isAboutPopup = savedInstanceState.getBoolean("isAboutPopup")
        level = savedInstanceState.getBoolean("level")
        targetMark = savedInstanceState.getInt("targetMark")
        level = savedInstanceState.getBoolean("level")

        whenRotateSet()
    }

    private fun whenRotateSet() {
        if (isNewgamePopup){
            mydialog!!.setContentView(R.layout.activity_game_inputs_rules)
            isNewgamePopup=true
            val startButton = mydialog!!.findViewById(R.id.start_button) as Button
            var target_markField = mydialog!!.findViewById(R.id.target_field) as EditText
            var user_nameField = mydialog!!.findViewById(R.id.name_field) as EditText
            val check_text = mydialog!!.findViewById(R.id.check) as TextView
            ishardswich = mydialog!!.findViewById(R.id.hardswich) as Switch

            ishardswich?.setOnCheckedChangeListener { _, isChecked ->
                level = isChecked
            }
            //
            user_nameField.setText("User")
            mydialog!!.setOnCancelListener{
                isNewgamePopup=false
            }
            target_markField.setText("101")
            startButton.setOnClickListener {
                if ((target_markField.text.isNotEmpty()) && (user_nameField.text.isNotEmpty())){
                    userName=user_nameField.text.toString()
                    targetMark= target_markField.text.toString().toInt()

                    val intent = Intent(this, GameScreen::class.java)
                    intent.putExtra("win_mark",targetMark)
                    intent.putExtra("user_name",userName)
                    intent.putExtra("ComputerWins",computerWins)
                    intent.putExtra("UserWins",userWins)
                    intent.putExtra("level",level)
                    startActivity(intent)
                    mydialog!!.dismiss()
                }else{
                    check_text.isVisible=true
                }
            }
            mydialog!!.show()
        }

        if (isAboutPopup){
            isAboutPopup=true
            mydialog!!.setContentView(R.layout.activity_popup_profile)
            mydialog!!.setOnCancelListener{
                isAboutPopup=false
            }
            mydialog!!.show()
        }
    }

    override fun onPause() {
        super.onPause()
        if (mydialog != null && mydialog!!.isShowing()) {
            mydialog!!.dismiss()
        }
    }
}