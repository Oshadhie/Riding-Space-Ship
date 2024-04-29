package com.example.ridingspaceship

import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.MotionEvent
import android.view.View

class GameViewActivity(var c: Context, var gameTask: GameTask) : View(c) {
    private var myPaint: Paint? = null
    private var speed = 1
    private var time = 0
    private var score = 0
    private var highScore = 0
    private var spacePosition = 0
    private val meteors = ArrayList<HashMap<String, Any>>()
    private var gameOver = false

    var viewWidth = 0
    var viewHeight = 0

    init {
        myPaint = Paint()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        viewWidth = this.measuredWidth
        viewHeight = this.measuredHeight

        if (!gameOver) {
            // Game is still ongoing
            if (time % 700 < 10 + speed) {
                val map = HashMap<String, Any>()
                map["lane"] = (0..2).random()
                map["startTime"] = time
                meteors.add(map)
            }
            time += 10 + speed
            var metWidth = viewWidth / 3
            val carHeight = metWidth + 10
            myPaint!!.style = Paint.Style.FILL
            val d = resources.getDrawable(R.drawable.spaceship, null)

            d.setBounds(
                spacePosition * viewWidth / 3 + viewWidth / 15 + 25,
                viewHeight - 2 - carHeight,
                spacePosition * viewWidth / 3 + viewWidth / 15 + metWidth - 25,
                viewHeight - 2
            )
            d.draw(canvas)
            myPaint!!.color = Color.GREEN

            for (i in meteors.indices) {
                try {
                    val carX = meteors[i]["lane"] as Int * viewWidth / 3 + viewWidth / 15
                    val carY = time - meteors[i]["startTime"] as Int
                    val d2 = resources.getDrawable(R.drawable.meteor, null)

                    d2.setBounds(
                        carX + 25, carY - carHeight, carX + metWidth - 25, carY
                    )
                    d2.draw(canvas)
                    if (meteors[i]["lane"] as Int == spacePosition) {
                        if (carY > viewHeight - 2 - carHeight
                            && carY < viewHeight - 2
                        ) {
                            gameTask.closeGame(score)
                            gameOver = true
                        }
                    }
                    if (carY > viewHeight + carHeight) {
                        meteors.removeAt(i)
                        score++;
                        speed = 1 + Math.abs(score / 8) // increase speed
                        if (score > highScore) {
                            highScore = score
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            myPaint!!.color = Color.WHITE
            myPaint!!.textSize = 60f
            canvas.drawText("Score:$score", 80f, 80f, myPaint!!)
            canvas.drawText("Speed:$speed",380f, 80f, myPaint!!)
            invalidate()
        } else {
            // Game over
            val intent = Intent(context, RestartActivity::class.java).apply {
                putExtra("score", score)
                putExtra("highScore", highScore)
            }
            context.startActivity(intent)
        }
    }

    fun resetGame() {
        score = 0
        speed = 1
        time = 0
        spacePosition = 0
        meteors.clear()
        invalidate()
        gameOver = false // Reset game over state
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event!!.action) {
            MotionEvent.ACTION_DOWN -> {
                if (gameOver) {
                    resetGame()
                } else {
                    // Handle touch input to move
                    val x1 = event.x
                    spacePosition = when {
                        x1 < viewWidth / 3 -> 0
                        x1 < (viewWidth * 2) / 3 -> 1
                        else -> 2
                    }
                    invalidate()
                }
            }
            MotionEvent.ACTION_UP -> {
            }
        }
        return true
    }
}
