/*package com.ebc.practice

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class CoffeeBreakActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tomato_dialog)

        val confirmButton: Button = findViewById(R.id.confirm_button)
        confirmButton.setOnClickListener {
            // Set result to indicate that coffee break is confirmed
            setResult(Activity.RESULT_OK)
            finish() // Close this activity and return to MainActivity
        }
    }
}

 */