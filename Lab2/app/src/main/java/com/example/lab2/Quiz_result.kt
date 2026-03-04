package com.example.lab2

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity


class Quiz_result : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_result)

        // Get score from intent
        val score = intent.getIntExtra("SCORE", 0)
        val maxScore = intent.getIntExtra("MAX_SCORE", 5)

        // Calculate percentage
        val percentage = (score.toFloat() / maxScore * 100).toInt()

        // Initialize views
        val scoreText = findViewById<TextView>(R.id.scoreText)
        val percentageText = findViewById<TextView>(R.id.percentageText)
        val ratingText = findViewById<TextView>(R.id.ratingText)
        val messageText = findViewById<TextView>(R.id.messageText)
        val retakeButton = findViewById<Button>(R.id.retakeButton)
        val homeButton = findViewById<Button>(R.id.homeButton)

        // Set score and percentage
        scoreText.text = "$score / $maxScore"
        percentageText.text = "$percentage%"

        // Determine rating and message based on score
        val (rating, message) = when (score) {
            5 -> Pair(
                "Perfect Score! 🎉",
                "Excellent! You have mastered mobile app development concepts. You're ready to build amazing apps!"
            )
            4 -> Pair(
                "Great Job! 👏",
                "Very good! You have a strong understanding of mobile development. Keep learning!"
            )
            3 -> Pair(
                "Good Effort! 👍",
                "You're on the right track! Review the concepts you missed and try again."
            )
            2 -> Pair(
                "Keep Trying! 📚",
                "You need more practice. Study the topics again and retake the quiz."
            )
            else -> Pair(
                "Need Improvement! 📖",
                "Don't give up! Review the material carefully and try again. Practice makes perfect!"
            )
        }

        ratingText.text = rating
        messageText.text = message

        // Set retake button click listener
        retakeButton.setOnClickListener {
           val intent=Intent(this,Quiz::class.java)

            startActivity(intent) // Start Quiz activity from new activity means previously done work is cleared
            finish() // Finish QuizResult activity and remove it from the back stack
        }

        // Set home button click listener
        homeButton.setOnClickListener {
            finishAffinity() // Close all activities and go to home
        }
    }
}