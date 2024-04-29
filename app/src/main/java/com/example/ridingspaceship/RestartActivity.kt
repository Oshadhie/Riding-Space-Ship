package com.example.ridingspaceship

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class RestartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restart)

        val score = intent.getIntExtra("score", 0)
        val highScore = intent.getIntExtra("highScore", 0)

        findViewById<TextView>(R.id.highScore).text = "High Score: $highScore"
        findViewById<TextView>(R.id.score).text = "Score: $score"

        findViewById<Button>(R.id.restartBtn).setOnClickListener {
            // Start the main activity to restart the game
            startActivity(Intent(this, MainActivity::class.java))
            finish() // Close the current activity
        }

        // Exit button
        findViewById<Button>(R.id.exitBtn).setOnClickListener {
            finishAffinity() // Close all activities and exit the application
        }
    }
}
