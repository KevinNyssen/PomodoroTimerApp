/*package com.ebc.practice

import android.app.Dialog
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class dummy : AppCompatActivity() {
    private var timeSelected: Int = 0
    private var timeCounted: CountDownTimer?=null
    private var timeProgress= 0
    private var pauseOffset: Long =0
    private var isStart=true



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val addBtn: ImageButton = findViewById(R.id.coffee_button)
        addBtn.setOnClickListener {
            //todo....
            setTimeFuction()
        }
        val startBtn: Button = findViewById(R.id.btn_Playpause)
        startBtn.setOnClickListener {
            startTimerSetup()
        }

    }
    private fun timePause(){
        if
    }




    private fun startTimerSetup(){
        val startBtn: Button = findViewById(R.id.btn_Playpause)
        if (timeSelected>timeProgress){
            if(isStart){
                startBtn.text="Pause"
                startTimer(pauseOffset)
                isStart=false
            }

            }
        }


    }



    private fun startTimer(pauseOffsetL: Long){
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        progressBar.progress = timeProgress
        timeCountDown=object : CountDownTimer(
            (timeSelected*60000).toLong() - pauseOffsetL*1000,1000)
        {
            override fun onTick(p0: Long) {
                timeProgress++
                pauseOffset=timeSelected.toLong()- p0/1000
                progressBar.progress=timeSelected-timeProgress
                val timeLeftTv: TextView = findViewById(R.id.TvTime_left)
                timeLeftTv.text = (timeSelected-timeProgress).toString()
            }
            override fun OnFinish(){
        }
    }



    private fun setTimer(){
        val timeDialog = Dialog(this)
        timeDialog.setContentView(R.layout.tomato_dialog)
        val timeSet = timeDialog.findViewById<EditText>(R.id.time_set)
        val timeLeftTv: TextView = findViewById(R.id.TvTime_left)
        val btnStart = Button = findViewById(R.id.btn_start)
        val progressBar = ProgressBar = findViewById(R.id.progressBar)
        timeDialog.findViewById<Button>(R.id.Ok).setOnClickListener {
            if (timeSet.text.isEmpty()){
                Toast.makeText(this,"Please enter time",Toast.LENGTH_SHORT).show()
            } else {
                timeLeftTv.text = timeSet.text
                btnStart.text = "Start"
                timeSelected = timeSet.text.toString().toInt()
                progressBar.max = timeSelected
            }
            timeDialog.dismiss()
        }
        timeDialog.show()
    }

}


 */