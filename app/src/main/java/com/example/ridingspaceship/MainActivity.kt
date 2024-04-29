package com.example.ridingspaceship

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(), GameTask {

    lateinit var rootLayout: LinearLayout
    lateinit var startBtn: Button
    lateinit var exitBtn: Button
    lateinit var mGameView: GameViewActivity
    lateinit var score: TextView
    lateinit var highScoreTextView: TextView
    private var highScore = 0
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var mediaPlayer: MediaPlayer // For background music

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // SharedPreferences for high scores
        sharedPreferences = getSharedPreferences("HighScorePrefs", Context.MODE_PRIVATE)
        highScore = sharedPreferences.getInt("highScore", 0)

        startBtn = findViewById(R.id.startBtn)
        exitBtn = findViewById(R.id.exitBtn)
        rootLayout = findViewById(R.id.rootLayout)
        score = findViewById(R.id.score)
        highScoreTextView = findViewById(R.id.highScore)
        mGameView = GameViewActivity(this, this)

        highScoreTextView.text = "High Score : $highScore"

        // Initialize MediaPlayer with background music
        mediaPlayer = MediaPlayer.create(this, R.raw.bacmusic)
        mediaPlayer.isLooping = true // Loop the background music
        mediaPlayer.start() // Start playing the background music

        // Start game
        startBtn.setOnClickListener {
            startGame()
        }

        // Exit game
        exitBtn.setOnClickListener {
            finishAffinity()
        }
    }

    // To start game
    private fun startGame() {
        mGameView.setBackgroundResource(R.drawable.background)
        rootLayout.addView(mGameView)
        startBtn.visibility = View.GONE
        exitBtn.visibility = View.GONE // not show the buttons and score TextView
        score.visibility = View.GONE
        highScoreTextView.visibility = View.GONE
    }

    // To close game
    override fun closeGame(mScore: Int) {

        score.text = "Score : $mScore"
        rootLayout.removeView(mGameView)

        // Check if the current score is high
        if (mScore > highScore) {
            highScore = mScore
            highScoreTextView.text = "High Score : $highScore"

            // Save the new high score to SharedPreferences
            sharedPreferences.edit().putInt("highScore", highScore).apply()
        }

        // Start RestartActivity and pass the score and high score as extras
        val intent = Intent(this, RestartActivity::class.java).apply {
            putExtra("score", mScore)
            putExtra("highScore", highScore)
        }
        startActivity(intent)

        // Finish the MainActivity
        finish()
    }

    // Stop the background music when the activity is destroyed
    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.stop()
        mediaPlayer.release()
    }
}
