package com.ebc.practice

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageButton
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

//something new
class MainActivity : AppCompatActivity() {

    private lateinit var timerTextView: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var controlButton: AppCompatButton
    private lateinit var resetButton: AppCompatButton
    private lateinit var coffeeButton: AppCompatButton

    private var isRunning = false
    private var isPaused = false
    private var countDownTimer: CountDownTimer? = null
    private val pomodoroDuration = 2 * 60 * 1000L // 25 minutes in milliseconds
    private val coffeBreakDuration = 5 * 60 * 1000L // 5 minutes in milliseconds
    private var timeLeftInMillis = pomodoroDuration // Track remaining time
    private val interval = 1000L // 1-second interval

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        enableEdgeToEdge()

        // Initialize views
        timerTextView = findViewById(R.id.timer)
        progressBar = findViewById(R.id.progress_bar)
        controlButton = findViewById(R.id.control_button)
        resetButton = findViewById(R.id.reset)

        // Set ProgressBar properties
        progressBar.max = (pomodoroDuration / interval).toInt()
        progressBar.progress = (timeLeftInMillis / interval).toInt()

        // Set initial timer text
        updateTimerText(timeLeftInMillis)

        // Control button functionality
        controlButton.setOnClickListener {
            when {
                !isRunning -> startTimer() // Initial start
                isPaused -> resumeTimer()  // Resume after pause
                else -> pauseTimer()        // Pause the timer if running
            }
        }

        // Reset button functionality
        resetButton.setOnClickListener {
            resetTimer()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun startTimer() {
        isRunning = true
        isPaused = false
        controlButton.text = getString(R.string.stop) // Set button to "Stop"

        // Set max to total time in seconds (adjusts the progress bar scale)
        progressBar.max = (pomodoroDuration / 1000).toInt()

        countDownTimer = object : CountDownTimer(timeLeftInMillis, interval) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftInMillis = millisUntilFinished
                updateTimerText(timeLeftInMillis)

                // Calculate elapsed time in seconds and set it as the progress
                val elapsedSeconds = ((pomodoroDuration - millisUntilFinished) / 1000).toInt()
                progressBar.progress = elapsedSeconds
                Log.d("ProgressUpdate", "Progress: $elapsedSeconds / ${progressBar.max}") // Log each progress update
                progressBar.invalidate()
            }

            override fun onFinish() {
                isRunning = false
                controlButton.text = getString(R.string.start) // Reset button to "Start"
                updateTimerText(0)
                progressBar.progress = progressBar.max // Complete progress on finish
                showRandomMessageDialog() // Optional end message
            }
        }.start()
    }

    private fun pauseTimer() {
        isPaused = true
        controlButton.text = getString(R.string.resume) // Change to "Resume"
        countDownTimer?.cancel() // Pause timer without resetting it
    }

    private fun resumeTimer() {
        isPaused = false
        controlButton.text = getString(R.string.stop) // Change back to "Stop" to allow pausing again
        startTimer() // Resume with the remaining time
    }

    private fun resetTimer() {
        countDownTimer?.cancel()
        isRunning = false
        isPaused = false
        timeLeftInMillis = pomodoroDuration
        controlButton.text = getString(R.string.start) // Reset to "Start"
        updateTimerText(pomodoroDuration)
        progressBar.progress = 0 // Reset progress bar to zero
    }

    private fun updateTimerText(millisUntilFinished: Long) {
        val minutes = (millisUntilFinished / 1000) / 60
        val seconds = (millisUntilFinished / 1000) % 60
        timerTextView.text = String.format("%02d:%02d", minutes, seconds)
    }

    private fun enableEdgeToEdge() {
        // Your existing code to handle edge-to-edge display
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun showRandomMessageDialog() {
        // Optional: Code to display a dialog with a random motivational message
    }
}