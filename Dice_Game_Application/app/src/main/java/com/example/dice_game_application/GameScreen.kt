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
    private val random = java.util.Random()
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
    var computerWins=0
    var userWins=0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_screen)

        mydialog= Dialog(this)

        //
        win_mark=intent.getIntExtra("win_mark",101)
        var user_name= intent.getStringExtra("user_name")
        computerWins=intent.getIntExtra("ComputerWins",0)
        userWins=intent.getIntExtra("UserWins",0)

        //connecting screen item with backend code
        val throwButton : Button = findViewById(R.id.throwbtn)
        val score : Button = findViewById(R.id.score)
        val cImage0 : ImageView = findViewById(R.id.c1)
        val cImage1 : ImageView = findViewById(R.id.c2)
        val cImage2 : ImageView = findViewById(R.id.c3)
        val cImage3 : ImageView = findViewById(R.id.c4)
        val cImage4 : ImageView = findViewById(R.id.c5)
        val uImage0 : ImageView = findViewById(R.id.u1)
        val uImage1 : ImageView = findViewById(R.id.u2)
        val uImage2 : ImageView = findViewById(R.id.u3)
        val uImage3 : ImageView = findViewById(R.id.u4)
        val uImage4 : ImageView = findViewById(R.id.u5)
        val computer_Score_text : TextView = findViewById(R.id.computer_score)
        val User_Score_text : TextView = findViewById(R.id.user_score)
        val round_text : TextView = findViewById(R.id.round)
        val target_text : TextView = findViewById(R.id.targetscore)
        val user_text : TextView = findViewById(R.id.user)
        val winUser_text : TextView = findViewById(R.id.userwins)
        val wincom_text : TextView = findViewById(R.id.computerwins)

        //List of dice faces
        userDiceFaces = mutableListOf(uImage0,uImage1,uImage2,uImage3,uImage4)
        computerDiceFaces = mutableListOf(cImage0,cImage1,cImage2,cImage3,cImage4)

        //initialize game screen
        target_text.setText(win_mark.toString())
        round_text.setText(rounds.toString())
        user_text.setText(user_name)
        winUser_text.setText(userWins.toString())
        wincom_text.setText(computerWins.toString())

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
            if (!isTie){
                score.isVisible=true
            }

            //Generating computer dice numbers
            computer_list=genarate_numbers()

            if (throwButton.text=="Throw"){
                if (!isTie){
                    throwButton.setText("Rethrow")
                    //user image set touch true
                    imageClickable(true)
                }

                //Generating user dice numbers
                user_list=genarate_numbers()

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

            if (isTie){
                //Increment rounds
                rounds++
                round_text.setText(rounds.toString())
                //updating score
                bothSideScoreUpdate()
                //set score in screen
                computer_Score_text.setText(computerscore.toString())
                User_Score_text.setText(userscore.toString())
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
        val userwinsText = mydialog.findViewById(R.id.resultuserwins) as TextView
        val comwinsText = mydialog.findViewById(R.id.resultcomwins) as TextView
        body.text = resultType
        userwinsText.text="H:"+userWins.toString()
        comwinsText.text="C:"+computerWins.toString()
        body.setTextColor(colour)

        mydialog.setCanceledOnTouchOutside(false)
        mydialog.setOnCancelListener {
            val intent = Intent(this, ChosenPage::class.java)
            intent.putExtra("ComputerWins",computerWins)
            intent.putExtra("UserWins",userWins)
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
        if (userscore >= win_mark || computerscore >= win_mark) {
            if (userscore > computerscore) {
                userWins++
                mydialog?.let { result_part(it, "You Win!", Color.GREEN) }
            } else if (userscore < computerscore) {
                computerWins++
                mydialog?.let { result_part(it, "You Lost", Color.RED) }
            } else {
                isTie = true
                whenTheGameTie()
            }
        }
    }

    private fun whenTheGameTie() {

    }

    //in this activity customize back button
    override fun onBackPressed() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Are you sure you want to exit?")
        builder.setCancelable(false)
        builder.setPositiveButton("Yes") { dialog, which ->
            val intent = Intent(this, ChosenPage::class.java)
            intent.putExtra("ComputerWins",computerWins)
            intent.putExtra("UserWins",userWins)
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