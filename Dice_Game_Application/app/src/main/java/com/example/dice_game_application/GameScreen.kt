package com.example.dice_game_application

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible

class GameScreen : AppCompatActivity() {
    val random = java.util.Random()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_screen)

        var user_list = mutableListOf<Int>()
        var computer_list = mutableListOf<Int>()

        val playbutton : Button = findViewById(R.id.throwbtn)
        val c_image1 : ImageView = findViewById(R.id.c1)
        val c_image2 : ImageView = findViewById(R.id.c2)
        val c_image3 : ImageView = findViewById(R.id.c3)
        val c_image4 : ImageView = findViewById(R.id.c4)
        val c_image5 : ImageView = findViewById(R.id.c5)
        val u_image1 : ImageView = findViewById(R.id.u1)
        val u_image2 : ImageView = findViewById(R.id.u2)
        val u_image3 : ImageView = findViewById(R.id.u3)
        val u_image4 : ImageView = findViewById(R.id.u4)
        val u_image5 : ImageView = findViewById(R.id.u5)
        val computer_Score_text : TextView = findViewById(R.id.computer_score)
        val User_Score_text : TextView = findViewById(R.id.user_score)
        val round_text : TextView = findViewById(R.id.round)
        val score : Button = findViewById(R.id.score)

        var computerscore=0
        var userscore=0
        var rounds=0

        var remove_list = mutableListOf<Boolean>(false,false,false,false,false)

        score.setOnClickListener{

            playbutton.setText("Throw")

            rounds++
            round_text.setText("Rounds "+rounds.toString())

            for (i in 0..4) {
                computerscore += computer_list[i]
                userscore += user_list[i]
            }

            computer_Score_text.setText(computerscore.toString())
            User_Score_text.setText(userscore.toString())

            score.isVisible=false
            u_image1.setBackgroundColor(Color.WHITE)
            u_image2.setBackgroundColor(Color.WHITE)
            u_image3.setBackgroundColor(Color.WHITE)
            u_image4.setBackgroundColor(Color.WHITE)
            u_image5.setBackgroundColor(Color.WHITE)

            u_image1.isClickable=false
            u_image2.isClickable=false
            u_image3.isClickable=false
            u_image4.isClickable=false
            u_image5.isClickable=false

            remove_list = mutableListOf<Boolean>(false,false,false,false,false)
        }

        playbutton.setOnClickListener {
            score.isVisible=true

            computer_list=genarate_numbers()

            if (playbutton.text=="Throw"){
                playbutton.setText("Rethrow")

                user_list=genarate_numbers()

                u_image1.isClickable=true
                u_image2.isClickable=true
                u_image3.isClickable=true
                u_image4.isClickable=true
                u_image5.isClickable=true

            }else if (playbutton.text=="Rethrow"){
                playbutton.setText("Rethrow again")

                for (i in 0 until 4) { // add 6 random numbers to the list
                    if (remove_list[i]==false){
                        user_list[i]=random.nextInt(6) + 1
                    }
                }

            }else{
                playbutton.setText("Throw")

                for (i in 0 until 4) { // add 6 random numbers to the list
                    if (remove_list[i]==false){
                        user_list[i]=random.nextInt(6) + 1
                    }
                }

                rounds++
                round_text.setText("Rounds "+rounds.toString())

                for (i in 0..4) {
                    computerscore += computer_list[i]
                    userscore += user_list[i]
                }

                computer_Score_text.setText(computerscore.toString())
                User_Score_text.setText(userscore.toString())

                score.isVisible=false

                u_image1.isClickable=false
                u_image2.isClickable=false
                u_image3.isClickable=false
                u_image4.isClickable=false
                u_image5.isClickable=false
            }

            setImage(c_image1,computer_list[0])
            setImage(c_image2,computer_list[1])
            setImage(c_image3,computer_list[2])
            setImage(c_image4,computer_list[3])
            setImage(c_image5,computer_list[4])

            setImage(u_image1,user_list[0])
            setImage(u_image2,user_list[1])
            setImage(u_image3,user_list[2])
            setImage(u_image4,user_list[3])
            setImage(u_image5,user_list[4])

            u_image1.setBackgroundColor(Color.WHITE)
            u_image2.setBackgroundColor(Color.WHITE)
            u_image3.setBackgroundColor(Color.WHITE)
            u_image4.setBackgroundColor(Color.WHITE)
            u_image5.setBackgroundColor(Color.WHITE)

            remove_list = mutableListOf<Boolean>(false,false,false,false,false)

        }

        u_image1.setOnClickListener{
            if (remove_list[0]==false){
                u_image1.setBackgroundColor(getColor(R.color.purple_500))
                remove_list[0]=true
            }else{
                u_image1.setBackgroundColor(Color.WHITE)
                remove_list[0]=false
            }
        }
        u_image2.setOnClickListener{
            if (remove_list[1]==false){
                u_image2.setBackgroundColor(getColor(R.color.purple_500))
                remove_list[1]=true
            }else{
                u_image2.setBackgroundColor(Color.WHITE)
                remove_list[1]=false
            }
        }
        u_image3.setOnClickListener{
            if (remove_list[2]==false){
                u_image3.setBackgroundColor(getColor(R.color.purple_500))
                remove_list[2]=true
            }else{
                u_image3.setBackgroundColor(Color.WHITE)
                remove_list[2]=false
            }
        }
        u_image4.setOnClickListener{
            if (remove_list[3]==false){
                u_image4.setBackgroundColor(getColor(R.color.purple_500))
                remove_list[3]=true
            }else{
                u_image4.setBackgroundColor(Color.WHITE)
                remove_list[3]=false
            }
        }
        u_image5.setOnClickListener{
            if (remove_list[4]==false){
                u_image5.setBackgroundColor(getColor(R.color.purple_500))
                remove_list[4]=true
            }else{
                u_image5.setBackgroundColor(Color.WHITE)
                remove_list[4]=false
            }
        }
    }

    fun genarate_numbers(): MutableList<Int> {
        val list = mutableListOf<Int>()

        for (i in 1..5) { // add 6 random numbers to the list
            val randomNumber = random.nextInt(6) + 1 // generate a random number between 1 and 6
            list.add(randomNumber)
        }
        return  list
    }


    fun setImage(image: ImageView, i: Int) {
        image.isVisible=true
        if (i==1){
            image.setImageResource(R.drawable.one)
        }else if (i==2){
            image.setImageResource(R.drawable.two)
        }else if (i==3){
            image.setImageResource(R.drawable.three)
        }else if (i==4){
            image.setImageResource(R.drawable.four)
        }else if (i==5){
            image.setImageResource(R.drawable.five)
        }else{
            image.setImageResource(R.drawable.six)
        }

    }

}