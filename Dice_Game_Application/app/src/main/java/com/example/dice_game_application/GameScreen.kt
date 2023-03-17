package com.example.dice_game_application

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible


class GameScreen : AppCompatActivity() {
    val random = java.util.Random()
    var mydialog: Dialog? = null

    //
    var user_list = mutableListOf<Int>()
    var computer_list = mutableListOf<Int>()

    //
    var remove_list = mutableListOf(false,false,false,false,false)

    //
    var computerscore=0
    var userscore=0
    var rounds=1
    var isTie=false

    //
    var userDiceFaces: MutableList<ImageView>? = null
    var computerDiceFaces: MutableList<ImageView>? = null

    //
    var win_mark=0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_screen)

        mydialog= Dialog(this)

        //
        win_mark=intent.getIntExtra("win_mark",101)
        val user_name= intent.getStringExtra("user_name")

        //connecting screen item with backend code
        val throwButton : Button = findViewById(R.id.throwbtn)
        val score : Button = findViewById(R.id.score)
        val c_image0 : ImageView = findViewById(R.id.c1)
        val c_image1 : ImageView = findViewById(R.id.c2)
        val c_image2 : ImageView = findViewById(R.id.c3)
        val c_image3 : ImageView = findViewById(R.id.c4)
        val c_image4 : ImageView = findViewById(R.id.c5)
        val u_image0 : ImageView = findViewById(R.id.u1)
        val u_image1 : ImageView = findViewById(R.id.u2)
        val u_image2 : ImageView = findViewById(R.id.u3)
        val u_image3 : ImageView = findViewById(R.id.u4)
        val u_image4 : ImageView = findViewById(R.id.u5)
        val computer_Score_text : TextView = findViewById(R.id.computer_score)
        val User_Score_text : TextView = findViewById(R.id.user_score)
        val round_text : TextView = findViewById(R.id.round)
        val target_text : TextView = findViewById(R.id.targetscore)
        val user_text : TextView = findViewById(R.id.user)

        //List of dice faces
        userDiceFaces = mutableListOf(u_image0,u_image1,u_image2,u_image3,u_image4)
        computerDiceFaces = mutableListOf(c_image0,c_image1,c_image2,c_image3,c_image4)

        //initialize game screen
        target_text.setText(win_mark.toString())
        round_text.setText(rounds.toString())
        user_text.setText(user_name)
        //
        for (imageIndex in 0 until 5){
            userDiceFaces!![imageIndex].setOnClickListener {
                remove_list[imageIndex]=selecImage(remove_list[imageIndex], userDiceFaces!![imageIndex])
            }
        }
        //user image set touch false
        imageClickable(false)
        //score button visibility off
        score.isVisible=false


        //when press the Score button
        score.setOnClickListener{
//            result_part(mydialog,"You Win !",Color.GREEN)
//            result_part(mydialog,"You Lost !",Color.RED)

            throwButton.setText("Throw")
            //Increment rounds
            rounds++
            round_text.setText(rounds.toString())

            //updating score
            bothSideScoreUpdate()

            //set score in screen
            computer_Score_text.setText(computerscore.toString())
            User_Score_text.setText(userscore.toString())

            //score button visibility off
            score.isVisible=false

            //set user images transparent
            imageTransparent()

            //user image set touch false
            imageClickable(false)

            // resetting the remove list
            remove_list = mutableListOf(false,false,false,false,false)

            //checking winning condition
            winningCondition()
        }

        //when press the Throw/Rethrow/Rethrow Again button
        throwButton.setOnClickListener {
            score.isVisible=true

            //Generating computer dice numbers
            computer_list=genarate_numbers()

            if (throwButton.text=="Throw"){
                throwButton.setText("Rethrow")

                //Generating user dice numbers
                user_list=genarate_numbers()

                //user image set touch true
                imageClickable(true)

            }else if (throwButton.text=="Rethrow"){
                throwButton.setText("Rethrow again")

                //giving random numbers not selected dice
                selectDiceUpdate()

            }else{
                throwButton.setText("Throw")
                //Increment rounds
                rounds++
                round_text.setText(rounds.toString())

                //giving random numbers not selected dice
                selectDiceUpdate()

                //updating score
                bothSideScoreUpdate()

                //set score in screen
                computer_Score_text.setText(computerscore.toString())
                User_Score_text.setText(userscore.toString())

                //score button visibility off
                score.isVisible=false

                //user image set touch false
                imageClickable(false)

                //checking winning condition
                winningCondition()
            }

            //set images in screen
            for (diceIndex in 0..4) {
                setImage(computerDiceFaces!![diceIndex],computer_list[diceIndex])
                setImage(userDiceFaces!![diceIndex],user_list[diceIndex])
            }

            //set user images transparent
            imageTransparent()

            // resetting the remove list
            remove_list = mutableListOf<Boolean>(false,false,false,false,false)
        }
    }

    //
    private fun imageTransparent() {
        for (userImage in userDiceFaces!!){
            userImage.setBackgroundColor(Color.TRANSPARENT)
        }
    }

    //
    private fun imageClickable(touch_status: Boolean) {
        for (image in userDiceFaces!!){
            image.isClickable=touch_status
        }
    }

    //
    private fun bothSideScoreUpdate() {
        for (i in 0..4) {
            computerscore += computer_list[i]
            userscore += user_list[i]
        }
    }

    //giving random numbers not selected dice
    private fun selectDiceUpdate() {
        for (i in 0 until 5) {
            if (remove_list[i]==false){
                user_list[i]=random.nextInt(6) + 1
                println(user_list[i])
            }
        }
    }

    //
    private fun selecImage(isImageSelect: Boolean, userImage: ImageView): Boolean {
        if (isImageSelect==false){
            userImage.setBackgroundColor(getColor(R.color.purple_500))
            return true
        }else{
            userImage.setBackgroundColor(Color.TRANSPARENT)
            return false
        }
    }

    //
    private fun result_part(mydialog: Dialog, resultType: String, colour: Int) {
        mydialog.setContentView(R.layout.activity_result_screen)
        val body = mydialog.findViewById(R.id.result) as TextView
        body.text = resultType
        body.setTextColor(colour)
        mydialog.setCanceledOnTouchOutside(false)
        mydialog.setOnCancelListener {
            val intent = Intent(this, ChosenPage::class.java)
            startActivity(intent)
        }
        mydialog.show()
    }

    //Generating dice numbers
    fun genarate_numbers(): MutableList<Int> {
        val list = mutableListOf<Int>()

        for (i in 1..5) {
            val randomNumber = random.nextInt(6) + 1
            list.add(randomNumber)
        }
        return  list
    }

    //
    fun setImage(image: ImageView, i: Int) {
        if (i==1){
            image.setImageResource(R.drawable.dice_1)
        }else if (i==2){
            image.setImageResource(R.drawable.dice_2)
        }else if (i==3){
            image.setImageResource(R.drawable.dice_3)
        }else if (i==4){
            image.setImageResource(R.drawable.dice_4)
        }else if (i==5){
            image.setImageResource(R.drawable.dice_5)
        }else{
            image.setImageResource(R.drawable.dice_6)
        }
    }

    //game winning condition with relevant rules
    private fun winningCondition() {
//        userscore = 95
//        computerscore=90
//        win_mark=200
//        if (userscore>=win_mark){
//            if (userscore==computerscore){
//                //tie
//                isTie=true
//            }else if (userscore>computerscore){
//                mydialog?.let { result_part(it,"You Win !",Color.GREEN) }
//            }else{
//                mydialog?.let { result_part(it,"You Lost !",Color.RED) }
//            }
//        }else if (computerscore>=win_mark){
//            if (userscore==computerscore){
//                //tie
//                isTie=true
//            }else if (computerscore>userscore){
//                mydialog?.let { result_part(it,"You Lost !",Color.RED) }
//            }else{
//                mydialog?.let { result_part(it,"You Win !",Color.GREEN) }
//            }
//        }

        if (userscore >= win_mark || computerscore >= win_mark) {
            if (userscore > computerscore) {
                mydialog?.let { result_part(it, "You Win!", Color.GREEN) }
            } else if (userscore < computerscore) {
                mydialog?.let { result_part(it, "You Lost!", Color.RED) }
            } else {
                isTie = true
                whenTheGameTie()
            }
        }
    }

    private fun whenTheGameTie() {

    }

    //in this activity customize backbutton
    override fun onBackPressed() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Are you sure you want to exit?")
        builder.setCancelable(false)
        builder.setPositiveButton("Yes") { dialog, which ->
            val intent = Intent(this, ChosenPage::class.java)
            startActivity(intent)
            finish()
        }
        builder.setNegativeButton("No") { dialog, which ->
            // Do nothing
        }
        val dialog = builder.create()
        dialog.show()
    }
}