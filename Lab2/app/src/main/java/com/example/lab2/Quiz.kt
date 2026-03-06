package com.example.lab2

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class Quiz : AppCompatActivity() {

    private lateinit var question1: RadioGroup
    private lateinit var question2: RadioGroup
    private lateinit var question3: RadioGroup
    private lateinit var question4: RadioGroup
    private lateinit var question5: RadioGroup
    private lateinit var submitButton: Button
    private lateinit var welcomeText: TextView
    private var username: String = ""

    // Data bank - Correct answers for each question
    companion object {
        const val CORRECT_Q1 = R.id.q1_option3  // Kotlin
        const val CORRECT_Q2 = R.id.q2_option2  // Model-View-ViewModel
        const val CORRECT_Q3 = R.id.q3_option4  // Intent
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
        welcomeText = findViewById(R.id.welcomeText)

        // Clear all selections when activity starts (for retake)
        clearAllSelections()

        // Check if username was passed from Welcome_Quiz
        username = intent.getStringExtra("USERNAME") ?: ""

        if (username.isEmpty()) {
            // If no username, show dialog to get it
            showUsernameDialog()
        } else {
            // Show welcome message with username
            showWelcomeMessage()
        }

        // Set click listener for submit button
        submitButton.setOnClickListener {
            submitQuiz()
        }
    }

    private fun showUsernameDialog() {
        // Create custom view for username dialog
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_username, null)
        val usernameInput = dialogView.findViewById<EditText>(R.id.usernameInput)

        // Create and show username dialog with custom styling
       val dialog = AlertDialog.Builder(this, R.style.RoundedDialog)
           .setTitle("🎯 Welcome to Quiz Master")
           .setMessage("Please enter your name to begin the challenge")
           .setView(dialogView)
           .setPositiveButton("START QUIZ", null) // Set to null initially
           .setNegativeButton("EXIT") { dialog, _ ->
               dialog.dismiss()
               finish() // Close quiz if user cancels
           }
           .setCancelable(false)
           .create()

       dialog.show()

       // Customize dialog colors
       dialog.window?.setBackgroundDrawableResource(android.R.color.holo_blue_dark) // Background color
       dialog.findViewById<TextView>(androidx.appcompat.R.id.alertTitle)?.setTextColor(resources.getColor(R.color.white, null)) // Title color
       dialog.findViewById<TextView>(android.R.id.message)?.setTextColor(resources.getColor(R.color.white, null)) // Message color

        // Override the positive button click listener to prevent auto-dismiss
        dialog.getButton(AlertDialog.BUTTON_POSITIVE)?.setOnClickListener {
            val enteredName = usernameInput.text.toString().trim()
            if (enteredName.isNotEmpty()) {
                username = enteredName
                dialog.dismiss()
                showWelcomeMessage()
                Toast.makeText(this, "Good luck, $username! 🎯", Toast.LENGTH_SHORT).show()
            } else {
                // Show error and don't dismiss the dialog
                Toast.makeText(this, "⚠️ Please enter your name", Toast.LENGTH_SHORT).show()
                usernameInput.error = "Name is required"
            }
        }

        // Customize button colors
        dialog.getButton(AlertDialog.BUTTON_POSITIVE)?.setTextColor(resources.getColor(android.R.color.holo_blue_dark, null))
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE)?.setTextColor(resources.getColor(android.R.color.darker_gray, null))

    }

    private fun showWelcomeMessage() {
        welcomeText.text = "👋 Welcome, $username!"
        welcomeText.visibility = android.view.View.VISIBLE
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
        intent.putExtra("USERNAME", username)
        startActivity(intent)
    }
}