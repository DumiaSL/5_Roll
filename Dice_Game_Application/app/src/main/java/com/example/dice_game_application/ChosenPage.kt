package com.example.dice_game_application

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible


class ChosenPage : AppCompatActivity() {

    var computerWins=0
    var userWins=0
    var mydialog1: Dialog? = null
    var mydialog2: Dialog? = null
    var isNewgamePopup=false
    var isAboutPopup=false
    var level = false
    lateinit var ishardswich: Switch
    lateinit var user_nameField: EditText
    lateinit var target_markField: EditText
    lateinit var check_text : TextView
    lateinit var buttoneffect : MediaPlayer
    var userName: String? ="User"
    var targetMark="101"
    var ischeckflagvisible=false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chosen_page)
        buttoneffect = MediaPlayer.create(this, R.raw.buttoneffect)

        PlayBackgroundSound()

        mydialog1=Dialog(this)
        mydialog2=Dialog(this)

        val about : Button = findViewById(R.id.about)
        val newgame : Button = findViewById(R.id.newgame)

        //new game pop id connect
        mydialog1!!.setContentView(R.layout.activity_game_inputs_rules)
        user_nameField = mydialog1!!.findViewById(R.id.name_field) as EditText
        target_markField = mydialog1!!.findViewById(R.id.target_field) as EditText
        check_text = mydialog1!!.findViewById(R.id.check) as TextView
        ishardswich = mydialog1!!.findViewById(R.id.hardswich) as Switch

        computerWins=intent.getIntExtra("ComputerWins",0)
        userWins=intent.getIntExtra("UserWins",0)

        if (userWins>0 || computerWins > 0){
            targetMark= intent.getIntExtra("targetMark",0).toString()
            userName= intent.getStringExtra("user_name")
        }

        if (savedInstanceState != null) {
            computerWins = savedInstanceState.getInt("computerWins")
            userWins = savedInstanceState.getInt("userWins")
            isNewgamePopup = savedInstanceState.getBoolean("isNewgamePopup")
            isAboutPopup = savedInstanceState.getBoolean("isAboutPopup")
            level = savedInstanceState.getBoolean("level")
            targetMark = savedInstanceState.getString("targetMark").toString()
            level = savedInstanceState.getBoolean("level")
            userName = savedInstanceState.getString("userName")
            ischeckflagvisible = savedInstanceState.getBoolean("ischeckflagvisible")
        }

        about.setOnClickListener {
            buttoneffect.start()
            isAboutPopup=true
            mydialog2!!.setContentView(R.layout.activity_popup_profile)
            mydialog2!!.setOnCancelListener{
                isAboutPopup=false
            }
            mydialog2!!.show()
        }

        newgame.setOnClickListener {
            buttoneffect.start()
            isNewgamePopup=true
            val startButton = mydialog1!!.findViewById(R.id.start_button) as Button

            ishardswich?.setOnCheckedChangeListener { _, isChecked ->
                level = isChecked
            }

            user_nameField.setText(userName)
            target_markField.setText(targetMark)

            mydialog1!!.setOnCancelListener{
                isNewgamePopup=false
            }

            startButton.setOnClickListener {
                if ((target_markField.text.isNotEmpty()) && (user_nameField.text.isNotEmpty()) && !(target_markField.text.toString().toInt()==0)){
                    buttoneffect.start()
                    userName=user_nameField.text.toString()
                    targetMark= target_markField.text.toString()

                    val intent = Intent(this, GameScreen::class.java)
                    intent.putExtra("win_mark",targetMark.toInt())
                    intent.putExtra("user_name",userName)
                    intent.putExtra("ComputerWins",computerWins)
                    intent.putExtra("UserWins",userWins)
                    intent.putExtra("level",level)
                    buttoneffect.release()
                    startActivity(intent)
                    finish()
                }else{
                    check_text.isVisible=true
                    ischeckflagvisible = true
                }
            }
            mydialog1!!.show()
        }
    }

    override fun onBackPressed() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Are you sure you want to exit?")
        builder.setCancelable(false)
        builder.setPositiveButton("Yes") { dialog, which ->
            buttoneffect.release()
            stopService(Intent(this, BackgroundSoundService::class.java))
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

        targetMark= target_markField.text.toString()

        outState.putString("targetMark", targetMark)
        userName= user_nameField.text.toString()
        outState.putString("userName", userName)
        outState.putBoolean("ischeckflagvisible", ischeckflagvisible)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        computerWins = savedInstanceState.getInt("computerWins")
        userWins = savedInstanceState.getInt("userWins")
        isNewgamePopup = savedInstanceState.getBoolean("isNewgamePopup")
        isAboutPopup = savedInstanceState.getBoolean("isAboutPopup")
        level = savedInstanceState.getBoolean("level")
        targetMark = savedInstanceState.getString("targetMark").toString()
        userName = savedInstanceState.getString("userName")
        ischeckflagvisible = savedInstanceState.getBoolean("ischeckflagvisible")

        whenRotateSet()
    }

    private fun whenRotateSet() {
        if (isNewgamePopup){
            user_nameField.setText(userName)
            target_markField.setText(targetMark)
            ishardswich.isChecked=level

            val startButton = mydialog1!!.findViewById(R.id.start_button) as Button
            var target_markField = mydialog1!!.findViewById(R.id.target_field) as EditText
            var user_nameField = mydialog1!!.findViewById(R.id.name_field) as EditText
            val check_text = mydialog1!!.findViewById(R.id.check) as TextView
            ishardswich = mydialog1!!.findViewById(R.id.hardswich) as Switch

            ishardswich?.setOnCheckedChangeListener { _, isChecked ->
                level = isChecked
            }


            if (ischeckflagvisible){
                check_text.isVisible=true
            }
            //
            mydialog1!!.setOnCancelListener{
                isNewgamePopup=false
            }
            startButton.setOnClickListener {
                if ((target_markField.text.isNotEmpty()) && (user_nameField.text.isNotEmpty()) && !(target_markField.text.toString().toInt()==0)){
                    buttoneffect.start()
                    userName=user_nameField.text.toString()
                    targetMark= target_markField.text.toString()

                    val intent = Intent(this, GameScreen::class.java)
                    intent.putExtra("win_mark",targetMark.toInt())
                    intent.putExtra("user_name",userName)
                    intent.putExtra("ComputerWins",computerWins)
                    intent.putExtra("UserWins",userWins)
                    intent.putExtra("level",level)
                    buttoneffect.release()
                    startActivity(intent)
                    finish()
                }else{
                    check_text.isVisible=true
                }
            }
            mydialog1!!.show()
        }

        if (isAboutPopup){
            mydialog2!!.setContentView(R.layout.activity_popup_profile)
            mydialog2!!.setOnCancelListener{
                isAboutPopup=false
            }
            mydialog2!!.show()
        }
    }

    override fun onResume() {
        super.onResume()
        startService(Intent(this, BackgroundSoundService::class.java))
    }

    override fun onPause() {
        super.onPause()
        stopService(Intent(this, BackgroundSoundService::class.java))

        if (mydialog1 != null && mydialog1!!.isShowing()) {
            mydialog1!!.dismiss()
        }
        if (mydialog2 != null && mydialog2!!.isShowing()) {
            mydialog2!!.dismiss()
        }
    }

    fun PlayBackgroundSound() {
        val backgroundtrack = Intent(this, BackgroundSoundService::class.java)
        startService(backgroundtrack)
    }

}