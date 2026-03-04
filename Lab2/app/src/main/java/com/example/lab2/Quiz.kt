package com.example.lab2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class Quiz : AppCompatActivity() {

    private lateinit var question1: RadioGroup
    private lateinit var question2: RadioGroup
    private lateinit var question3: RadioGroup
    private lateinit var question4: RadioGroup
    private lateinit var question5: RadioGroup
    private lateinit var submitButton: Button

    // Data bank - Correct answers for each question
    companion object {
        const val CORRECT_Q1 = R.id.q1_option3  // Kotlin
        const val CORRECT_Q2 = R.id.q2_option2  // Model-View-ViewModel
        const val CORRECT_Q3 = R.id.q3_option4  // All of the above
        const val CORRECT_Q4 = R.id.q4_option1  // onCreate()
        const val CORRECT_Q5 = R.id.q5_option3  // RecyclerView
        const val TOTAL_QUESTIONS = 5
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        // Initialize views
        question1 = findViewById(R.id.question1)
        question2 = findViewById(R.id.question2)
        question3 = findViewById(R.id.question3)
        question4 = findViewById(R.id.question4)
        question5 = findViewById(R.id.question5)
        submitButton = findViewById(R.id.submitButton)

        // Clear all selections when activity starts (for retake)
        clearAllSelections()

        // Set click listener for submit button
        submitButton.setOnClickListener {
            submitQuiz()
        }
    }

    private fun clearAllSelections() {
        question1.clearCheck()
        question2.clearCheck()
        question3.clearCheck()
        question4.clearCheck()
        question5.clearCheck()
    }

    private fun submitQuiz() {
        // Check if all questions are answered
        if (question1.checkedRadioButtonId == -1 ||
            question2.checkedRadioButtonId == -1 ||
            question3.checkedRadioButtonId == -1 ||
            question4.checkedRadioButtonId == -1 ||
            question5.checkedRadioButtonId == -1) {

            Toast.makeText(this, "Please answer all questions!", Toast.LENGTH_SHORT).show()
            return
        }

        // Calculate score by checking correct answers
        var score = 0

        // Question 1: What is the primary language for Android development?
        if (question1.checkedRadioButtonId == CORRECT_Q1) {
            score++
        }

        // Question 2: What does MVVM stand for in Android architecture?
        if (question2.checkedRadioButtonId == CORRECT_Q2) {
            score++
        }

        // Question 3: Which of the following is NOT a mobile app development platform?
        if (question3.checkedRadioButtonId == CORRECT_Q3) {
            score++
        }

        // Question 4: Which method is called when an Activity is first created?
        if (question4.checkedRadioButtonId == CORRECT_Q4) {
            score++
        }

        // Question 5: What is used to display a scrollable list in Android?
        if (question5.checkedRadioButtonId == CORRECT_Q5) {
            score++
        }

        // Navigate to result page with score
        val intent = Intent(this, Quiz_result::class.java)
        intent.putExtra("SCORE", score)
        intent.putExtra("MAX_SCORE", TOTAL_QUESTIONS)
        startActivity(intent)
    }
}