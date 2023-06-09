package com.example.dice_game_application

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.view.View
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
    var userRemoveList = mutableListOf(false,false,false,false,false)

    //
    var computerscore=0
    var userscore=0
    var rounds=0
    var targetMark=101
    var user_name = ""
    var level=false
    var computerWins=0
    var userWins=0
    var isTie=false
    var throwRound=0
    var isWinPopUp: String? =null

    //
    var userDiceFaces: MutableList<ImageView>? = null
    var computerDiceFaces: MutableList<ImageView>? = null


    lateinit var target_text: TextView
    lateinit var computer_Score_text: TextView
    lateinit var User_Score_text: TextView
    lateinit var round_text: TextView
    lateinit var throwButton: Button
    lateinit var score: Button

    lateinit var diceselecteffect : MediaPlayer
    lateinit var gamewinsound : MediaPlayer
    lateinit var gamelostsound : MediaPlayer
    lateinit var buttloneffect : MediaPlayer
    lateinit var dicerolleffect : MediaPlayer


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_screen)
        buttloneffect = MediaPlayer.create(this, R.raw.buttoneffect)
        dicerolleffect = MediaPlayer.create(this, R.raw.dicerolleffect)
        diceselecteffect = MediaPlayer.create(this, R.raw.diceselect)
        gamewinsound = MediaPlayer.create(this, R.raw.gamewinsound)
        gamelostsound = MediaPlayer.create(this, R.raw.losesoundeffects)


        mydialog= Dialog(this)

        //
        targetMark=intent.getIntExtra("win_mark",101)
        user_name= intent.getStringExtra("user_name").toString()
        computerWins=intent.getIntExtra("ComputerWins",0)
        userWins=intent.getIntExtra("UserWins",0)
        level=intent.getBooleanExtra("level",false)


        //connecting screen item with backend code
        throwButton = findViewById(R.id.throwbtn)
        score = findViewById(R.id.score)
        computer_Score_text = findViewById(R.id.computer_score)
        User_Score_text = findViewById(R.id.user_score)
        round_text = findViewById(R.id.round)
        target_text = findViewById(R.id.targetscore)
        val user_text : TextView = findViewById(R.id.user)
        val winUser_text : TextView = findViewById(R.id.userwins)
        val wincom_text : TextView = findViewById(R.id.computerwins)


        //List of dice faces
        computerDiceFaces = mutableListOf(findViewById(R.id.c1),findViewById(R.id.c2),findViewById(R.id.c3), findViewById(R.id.c4),findViewById(R.id.c5))
        userDiceFaces = mutableListOf(findViewById(R.id.u1),findViewById(R.id.u2),findViewById(R.id.u3),findViewById(R.id.u4),findViewById(R.id.u5))

        //
        if (savedInstanceState != null) {
            computerscore = savedInstanceState.getInt("computerscore")
            userscore = savedInstanceState.getInt("userscore")
            rounds = savedInstanceState.getInt("rounds")
            computerWins=savedInstanceState.getInt("computerWins")
            userWins=savedInstanceState.getInt("userWins")
            isTie= savedInstanceState.getBoolean("isTie")
            throwRound=savedInstanceState.getInt("throwRound")
            isWinPopUp=savedInstanceState.getString("isWinPopUp")

            user_list= savedInstanceState.getIntegerArrayList("user_list") as MutableList<Int>
            computer_list= savedInstanceState.getIntegerArrayList("computer_list") as MutableList<Int>
            var tempRemoveList=savedInstanceState.getIntegerArrayList("tempRemoveList") as MutableList<Int>
            for (index in 0..4){
                if (tempRemoveList[index]==0){
                    userRemoveList[0]=false
                }else{
                    userRemoveList[index]=true
                }
            }
        }

        //initialize game screen
        target_text.setText(targetMark.toString())
        round_text.setText(rounds.toString())
        user_text.setText(user_name)
        winUser_text.setText("H:"+userWins.toString())
        wincom_text.setText("C:"+computerWins.toString())
        //
        for (imageIndex in 0 until 5){
            userDiceFaces!![imageIndex].setOnClickListener {
                userRemoveList[imageIndex]=selecImage(userRemoveList[imageIndex], userDiceFaces!![imageIndex])
            }
        }
        //user image set touch false
        imageClickable(false)
        //score button visibility off
        score.isVisible=false

        //when press the Score button
        score.setOnClickListener{
            buttloneffect.start()
            //random strategy twice for computer
            if (!level){
                randomComputer()
                randomComputer()
            }else{
                hardComputerPlayerStrategy()
            }

            throwButton.setText("Throw")

            //set images in screen
            for (diceIndex in 0..4) {
                setImage(computerDiceFaces!![diceIndex],computer_list[diceIndex])
            }

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
            userRemoveList = mutableListOf(false,false,false,false,false)

            //checking winning condition
            winningCondition()
        }

        //when press the Throw/Rethrow/Rethrow Again button
        throwButton.setOnClickListener {
            buttloneffect.start()
            dicerolleffect.start()

            if (!isTie){
                score.isVisible=true
            }

            //Generating computer dice numbers
            if (throwRound==0){
                //Increment rounds
                rounds++
                round_text.setText(rounds.toString())

                if (!isTie){
                    throwButton.setText("Rethrow")
                    throwRound++
                    //user image set touch true
                    imageClickable(true)
                }
                //Generate computer numbers
                computer_list=genarate_numbers()
                //Generating user dice numbers
                user_list=genarate_numbers()

            }else if (throwRound==1){
                throwButton.setText("Rethrow again")

                //giving random numbers not selected dice
                selectDiceUpdate(userRemoveList,user_list)

                throwRound++

            }else{
                //random strategy twice for computer
                if (!level){
                    randomComputer()
                    randomComputer()
                }else{
                    hardComputerPlayerStrategy()
                }

                throwButton.setText("Throw")
                throwRound=0

                //giving random numbers not selected dice
                selectDiceUpdate(userRemoveList, user_list)

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
            userRemoveList = mutableListOf<Boolean>(false,false,false,false,false)
        }
    }

    private fun hardComputerPlayerStrategy() {
        var tempcomputerscore=computerscore
        var gap = userscore - tempcomputerscore

        //throw
        if (gap > 20){
            //high
            //checking high values
            for (count in  0 ..4){
                var randomindex = random.nextInt(5)
                if (computer_list[randomindex] <= 3){
                    computer_list[randomindex]=random.nextInt(6) + 1
                }
            }
        }else if(gap < 20){
            randomComputer()
            randomComputer()
        }else {
            //normal
            randomComputer()
            for (count in  0 ..4){
                var randomindex = random.nextInt(5)
                if (computer_list[randomindex] <= 2){
                    computer_list[randomindex]=random.nextInt(6) + 1
                }
            }
        }

        for (i in 0..4) {
            tempcomputerscore += computer_list[i]
        }

        //second throw
        if ((tempcomputerscore<(userscore+10))){
            var gap = userscore - tempcomputerscore

            if (gap > 20){
                //high
                //checking high values
                for (count in  0 ..4){
                    var randomindex = random.nextInt(5) + 1
                    if (computer_list[randomindex] <= 3){
                        computer_list[randomindex]=random.nextInt(6) + 1
                    }
                }
            }else if(gap < 20){
                randomComputer()
                randomComputer()
            }else {
                //normal
                //normal
                randomComputer()
                for (count in  0 ..4){
                    var randomindex = random.nextInt(5) + 1
                    if (computer_list[randomindex] <= 2){
                        computer_list[randomindex]=random.nextInt(6) + 1
                    }
                }
            }
        }
    }

    private fun whenRotateSet() {
        computer_Score_text.setText(computerscore.toString())
        User_Score_text.setText(userscore.toString())
        round_text.setText(rounds.toString())

        if (!(computer_list.size==0)){
            //set images in screen
            for (diceIndex in 0..4) {
                setImage(computerDiceFaces!![diceIndex],computer_list[diceIndex])
                setImage(userDiceFaces!![diceIndex],user_list[diceIndex])
            }
        }

        if (throwRound==0){
            throwButton.setText("Throw")
        }   else if (throwRound==1){
            throwButton.setText("Rethrow")
        }else{
            throwButton.setText("Rethrow again")
        }

        if (isTie || throwRound==0){
            score.isVisible=false
            //user image set touch false
            imageClickable(false)
        }else{
            score.isVisible=true
            //user image set touch false
            imageClickable(true)
        }

        for (imageIndex in 0 until 5){
            if (userRemoveList[imageIndex]==false){
                userDiceFaces?.get(imageIndex)?.setBackgroundColor(Color.TRANSPARENT)
            }else{
                userDiceFaces?.get(imageIndex)?.setBackgroundColor(getColor(R.color.purple_500))
            }
        }

        if (isWinPopUp=="win"){
            mydialog?.let { result_part(it, "You Win!", Color.GREEN) }
        }else if (isWinPopUp=="lost"){
            mydialog?.let { result_part(it, "You Lost", Color.RED) }
        }
    }

    //The computer player follows a random strategy
    private fun randomComputer() {
        //computer algorithm
        if (listOf(true, false).random()){
            var temp_list= mutableListOf<Boolean>()
            for (count in 0 .. 4){
                temp_list.add(listOf(true, false).random())
            }
            selectDiceUpdate(temp_list,computer_list)
        }
    }

    //set image transparent
    private fun imageTransparent() {
        for (userImage in userDiceFaces!!){
            userImage.setBackgroundColor(Color.TRANSPARENT)
        }
    }

    //set image transparent true or false
    private fun imageClickable(touch_status: Boolean) {
        for (image in userDiceFaces!!){
            image.isClickable=touch_status
        }
    }

    //update both side score
    private fun bothSideScoreUpdate() {
        for (i in 0..4) {
            computerscore += computer_list[i]
            userscore += user_list[i]
        }
    }

    //giving random numbers not selected dice
    private fun selectDiceUpdate(
        RemoveList: MutableList<Boolean>,
        type_list: MutableList<Int>
    ) {
        for (i in 0 until 5) {
            if (RemoveList[i]==false){
                type_list[i]=random.nextInt(6) + 1
            }
        }
    }

    //select image
    private fun selecImage(isImageSelect: Boolean, userImage: ImageView): Boolean {
        if (isImageSelect==false){
            userImage.setBackgroundColor(getColor(R.color.purple_500))
            diceselecteffect.start()
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
            intent.putExtra("ComputerWins",computerWins)
            intent.putExtra("UserWins",userWins)
            intent.putExtra("user_name",user_name)
            intent.putExtra("targetMark",targetMark)
            startActivity(intent)
            finish()
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
        if (userscore >= targetMark || computerscore >= targetMark) {
            if (userscore > computerscore) {
                isWinPopUp="win"
                userWins++
                gamewinsound.start()
                mydialog?.let { result_part(it, "You Win!", Color.GREEN) }
            } else if (userscore < computerscore) {
                isWinPopUp="lost"
                computerWins++
                gamelostsound.start()
                mydialog?.let { result_part(it, "You Lost", Color.RED) }
            } else {
                isTie = true
            }
        }
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
            intent.putExtra("user_name",user_name)
            intent.putExtra("targetMark",targetMark)
            buttloneffect.release()
            dicerolleffect.release()
            diceselecteffect.release()
            gamewinsound.release()
            gamelostsound.release()
            startActivity(intent)
            finish()
        }
        builder.setNeutralButton("Yes, Game Quit") { dialog, which ->
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
        outState.putInt("computerscore", computerscore)
        outState.putInt("userscore", userscore)
        outState.putInt("rounds", rounds)
        outState.putInt("computerWins", computerWins)
        outState.putInt("userWins", userWins)
        outState.putBoolean("isTie", isTie)
        outState.putIntegerArrayList("user_list", ArrayList(user_list))
        outState.putIntegerArrayList("computer_list", ArrayList(computer_list))

        outState.putInt("throwRound", throwRound)
        outState.putString("isWinPopUp", isWinPopUp)

        var tempRemoveList = mutableListOf<Int>()
        for (index in 0..4){
            if (userRemoveList[index]==true){
                tempRemoveList.add(1)
            }else{
                tempRemoveList.add(0)
            }
        }
        outState.putIntegerArrayList("tempRemoveList", ArrayList(tempRemoveList))
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        computerscore = savedInstanceState.getInt("computerscore")
        userscore = savedInstanceState.getInt("userscore")
        rounds = savedInstanceState.getInt("rounds")
        computerWins=savedInstanceState.getInt("computerWins")
        userWins=savedInstanceState.getInt("userWins")
        isTie= savedInstanceState.getBoolean("isTie")
        user_list= savedInstanceState.getIntegerArrayList("user_list") as MutableList<Int>
        computer_list= savedInstanceState.getIntegerArrayList("computer_list") as MutableList<Int>
        var tempRemoveList=savedInstanceState.getIntegerArrayList("tempRemoveList") as MutableList<Int>
        throwRound=savedInstanceState.getInt("throwRound")
        isWinPopUp=savedInstanceState.getString("isWinPopUp")

        for (index in 0..4){
            if (tempRemoveList[index]==0){
                userRemoveList[index]=false
            }else{
                userRemoveList[index]=true
            }
        }

        whenRotateSet()
    }
    override fun onResume() {
        super.onResume()
        startService(Intent(this, BackgroundSoundService::class.java))
    }

    override fun onPause() {
        super.onPause()
        stopService(Intent(this, BackgroundSoundService::class.java))
        if (mydialog != null && mydialog!!.isShowing()) {
            mydialog!!.dismiss()
        }
    }
}