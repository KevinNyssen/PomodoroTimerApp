package com.ebc.practice

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class CheckpointActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.checkpoint)

        // Set the random message
        val randomMessage = RandomMessages.getRandomMessage()
        findViewById<TextView>(R.id.random_message).text = randomMessage

        // Retrieve the state from MainActivity
        val isPomodoro = intent.getBooleanExtra("isPomodoro", true)
        val remainingTime = intent.getLongExtra("remainingTime", 0L)

        // Get the views to change based on the timer mode
        val checkpointImage: ImageView = findViewById(R.id.checkpoint_image)
        val timeText: TextView = findViewById(R.id.time_text)

        if (isPomodoro) {
            // Set to Pomodoro display
            checkpointImage.setImageResource(R.drawable.coffe_up)
            timeText.setText(R.string.coffee_time)
        } else {
            // Set to coffee break display
            checkpointImage.setImageResource(R.drawable.tomato_slice)
            timeText.setText(R.string.tomato_time)
        }

        val confirmButton: Button = findViewById(R.id.confirm_button)
        confirmButton.setOnClickListener {
            // Return to MainActivity with result
            val resultIntent = Intent()
            setResult(RESULT_OK, resultIntent)
            finish()
        }
    }
}
