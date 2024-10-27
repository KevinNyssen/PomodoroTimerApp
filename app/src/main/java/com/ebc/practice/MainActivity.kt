package com.ebc.practice

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageButton
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

//Version control beta 1.0.1
//Nav into CheckpointActivity
class MainActivity : AppCompatActivity() {
    private val REQUEST_CODE_CHECKPOINT = 1
    private var isPomodoro = true
    private var originalTimeLeftInMillis = 0L // Store the main timer's remaining time
    private var isInitialized = false



    private lateinit var timerTextView: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var controlButton: AppCompatButton
    private lateinit var resetButton: AppCompatButton
    private lateinit var coffeeButton: ImageButton

    private var isRunning = false
    private var isPaused = false
    private var countDownTimer: CountDownTimer? = null
    private val pomodoroDuration = 25 * 60 * 1000L // 25 minutes in milliseconds
    private val coffeBreakDuration = 5* 60 * 1000L // 5 minutes in milliseconds
    private var slices = 0 // Initialize the slice counter
    private val fullBreakDuration: Long = 30 * 60 * 1000 // 30 minutes in milliseconds
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
        coffeeButton = findViewById(R.id.coffee_button)

        // Set ProgressBar properties
        progressBar.max = (pomodoroDuration / interval).toInt()
        progressBar.progress = (timeLeftInMillis / interval).toInt()

        // Set initial timer text
        updateTimerText(timeLeftInMillis)

        // Control button functionality
        controlButton.setOnClickListener {
            when {
                !isRunning && !isInitialized -> {
                    isInitialized = true // Mark timer as started at least once
                    startTimer()
                }
                isPaused -> resumeTimer()  // Resume after pause
                else -> pauseTimer()        // Pause the timer if running
            }
        }

        // Reset button functionality
        resetButton.setOnClickListener {
            resetTimer()
        }
        // Coffee button functionality
        coffeeButton.setOnClickListener {
            // Store the remaining time in a separate variable
            originalTimeLeftInMillis = timeLeftInMillis

            //done: Navigate to CheckpointActivity

            // Navigate to CheckpointActivity
            slices++ // Increment slices counter
            navigateToCheckpoint(isPomodoro)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    // Handle navigation to CheckpointActivity on Pomodoro completion
    private fun startPomodoroTimer() {
        isRunning = true
        isPomodoro = true // Set mode to Pomodoro
        coffeeButton.isEnabled = true
        controlButton.text = getString(R.string.stop) // Set button to "Stop"
        progressBar.max = (pomodoroDuration / interval).toInt() // Set max progress for Pomodoro
        countDownTimer?.cancel() // Cancel any existing timer

        countDownTimer = object : CountDownTimer(timeLeftInMillis, interval) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftInMillis = millisUntilFinished
                updateTimerText(timeLeftInMillis)

                // Update progress bar for Pomodoro timer
                val progress = ((pomodoroDuration - millisUntilFinished) / interval).toInt()
                progressBar.progress = progress
            }

            override fun onFinish() {
                isRunning = false
                timeLeftInMillis = pomodoroDuration
                updateTimerText(0)
                progressBar.progress = progressBar.max

                slices++ // Increment slices counter
                navigateToCheckpoint(isPomodoro)
            }
        }.start()
    }

    private fun startCoffeeBreakTimer() {
        isRunning = true
        isPomodoro = false
        controlButton.text = getString(R.string.stop) // Set button to "Stop"
        progressBar.max = (coffeBreakDuration / interval).toInt() // Set max progress for Coffee Break
        countDownTimer?.cancel()
        coffeeButton.isEnabled = false

        countDownTimer = object : CountDownTimer(timeLeftInMillis, interval) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftInMillis = millisUntilFinished
                updateTimerText(timeLeftInMillis)

                // Update progress bar for Coffee Break timer
                val progress = ((coffeBreakDuration - millisUntilFinished) / interval).toInt()
                progressBar.progress = progress
            }

            override fun onFinish() {
                isRunning = false
                timeLeftInMillis = coffeBreakDuration
                updateTimerText(0)
                progressBar.progress = progressBar.max
                navigateToCheckpoint(isPomodoro)// Toggle back to Pomodoro for next cycle
                coffeeButton.isEnabled = true

            }
        }.start()
    }
    // New function to handle the full break timer
    private fun startFullBreakTimer() {
        isRunning = true
        isPomodoro = false
        controlButton.text = getString(R.string.stop) // Set button to "Stop"
        progressBar.max = (fullBreakDuration / interval).toInt() // Set max progress for Full Break
        countDownTimer?.cancel()
        coffeeButton.isEnabled = false

        countDownTimer = object : CountDownTimer(fullBreakDuration, interval) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftInMillis = millisUntilFinished
                updateTimerText(timeLeftInMillis)

                // Update progress bar for Full Break timer
                val progress = ((fullBreakDuration - millisUntilFinished) / interval).toInt()
                progressBar.progress = progress
            }

            override fun onFinish() {
                isRunning = false
                timeLeftInMillis = fullBreakDuration
                updateTimerText(0)
                progressBar.progress = progressBar.max // Full break complete
                coffeeButton.isEnabled = true

                // Reset slices after full break
                slices = 0
            }
        }.start()
    }

    private fun resumeTimer() {
        isPaused = false
        controlButton.text = getString(R.string.stop)

        // Start the appropriate timer based on current state
        if (isPomodoro) {
            startPomodoroTimer() // Resume as Pomodoro if it was in Pomodoro mode
        } else {
            // Determine if the next cycle is a coffee break or full break based on slices
            if (slices < 4) {
                Log.d("PomodoroTimer", "Starting Full Break Timer")
                startCoffeeBreakTimer() // Resume as Coffee Break if it was in break mode
            } else {
                startFullBreakTimer() // Start full break if slices reached 4
            }
        }
    }
    private fun updateDots() {
        val dotViews = listOf(
            findViewById<ImageView>(R.id.dot1),
            findViewById<ImageView>(R.id.dot2),
            findViewById<ImageView>(R.id.dot3),
            findViewById<ImageView>(R.id.dot4)
        )

        // Loop through each dot view and update the drawable based on the slices count
        for (i in dotViews.indices) {
            if (i < slices+1) {
                dotViews[i].setImageResource(R.drawable.red_dot) // Change to red if within slice count
            } else {
                dotViews[i].setImageResource(R.drawable.gray_dot) // Keep gray if above slice count
            }
        }
    }


    // Helper function to start the appropriate timer based on isPomodoro
    private fun startTimer() {
        if (isPomodoro) {
            coffeeButton.isEnabled = true
            updateDots()
            startPomodoroTimer()
        } else {
            // Check if the next cycle is a coffee break or full break
            if (slices < 4) {
                Log.d("PomodoroTimer", "Starting Coffee Break Timer")
                startCoffeeBreakTimer()
            } else {
                Log.d("PomodoroTimer", "Starting Full Break Timer")
                startFullBreakTimer()
                updateDots()
            }
        }
        // Update the dot indicators based on the number of slices
    }

    private fun pauseTimer() {
        isPaused = true
        controlButton.text = getString(R.string.resume)
        countDownTimer?.cancel() // Pause the timer without resetting it
    }


    // Handle result from CheckpointActivity
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_CHECKPOINT && resultCode == RESULT_OK) {
            isPomodoro = !isPomodoro
            timeLeftInMillis = if (isPomodoro) pomodoroDuration else coffeBreakDuration
            progressBar.max = (timeLeftInMillis / interval).toInt()
            startTimer() // Start the appropriate timer
        }
    }


    private fun resetTimer() {
        countDownTimer?.cancel()
        isInitialized = false
        isRunning = false
        isPaused = false
        coffeeButton.isEnabled = false
        isPomodoro = true // Reset to Pomodoro mode
        slices=0
        timeLeftInMillis = pomodoroDuration
        progressBar.max = (pomodoroDuration / interval).toInt()
        progressBar.progress = 0 // Reset progress bar to zero
        updateTimerText(timeLeftInMillis)
        controlButton.text = getString(R.string.start)
    }

    private fun updateTimerText(millisUntilFinished: Long) {
        val minutes = (millisUntilFinished / 1000) / 60
        var seconds = (millisUntilFinished / 1000) % 60
        seconds++
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


    private fun navigateToCheckpoint(isPomodoro: Boolean) {
        val intent = Intent(this, CheckpointActivity::class.java).apply {
            putExtra("isPomodoro", isPomodoro)
            putExtra("remainingTime", timeLeftInMillis)
        }
        startActivityForResult(intent, REQUEST_CODE_CHECKPOINT)
    }

    private fun showRandomMessageDialog() {
        // Optional: Code to display a dialog with a random motivational message
    }
}