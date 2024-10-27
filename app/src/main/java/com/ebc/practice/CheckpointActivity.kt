package com.ebc.practice

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class CheckpointActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.checkpoint)

        val confirmButton: Button = findViewById(R.id.confirm_button)
        confirmButton.setOnClickListener {
            // Return to MainActivity with the result
            val intent = Intent()
            setResult(RESULT_OK, intent)
            finish() // Close this activity
        }
    }
}
