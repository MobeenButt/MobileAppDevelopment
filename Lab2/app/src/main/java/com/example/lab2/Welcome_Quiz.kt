package com.example.lab2

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Welcome_Quiz : AppCompatActivity() {

    private lateinit var startQuizButton: Button
    private var username: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_welcome_quiz)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize button
        startQuizButton = findViewById(R.id.startQuizButton)

        // Set click listener for start quiz button
        startQuizButton.setOnClickListener {
            showUsernameDialog()
        }
    }

    private fun showUsernameDialog() {
        // Create custom view for username dialog
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_username, null)
        val usernameInput = dialogView.findViewById<EditText>(R.id.usernameInput)

        // Create and show username dialog
        AlertDialog.Builder(this)
            .setTitle("Welcome!")
            .setMessage("Please enter your name to continue")
            .setView(dialogView)
            .setPositiveButton("Continue") { dialog, _ ->
                val enteredName = usernameInput.text.toString().trim()
                if (enteredName.isNotEmpty()) {
                    username = enteredName
                    dialog.dismiss()
                    showStartQuizDialog()
                } else {
                    Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .setCancelable(false)
            .show()
    }

    private fun showStartQuizDialog() {
        // Create and show quiz start confirmation dialog
        AlertDialog.Builder(this)
            .setTitle("Ready to Start?")
            .setMessage("Hello $username! 👋\n\nYou're about to start a quiz with 5 questions on Mobile App Development.\n\nAre you ready?")
            .setPositiveButton("Start Quiz") { dialog, _ ->
                dialog.dismiss()
                // Navigate to Quiz activity
                val intent = Intent(this, Quiz::class.java)
                intent.putExtra("USERNAME", username)
                startActivity(intent)
            }
            .setNegativeButton("Not Yet") { dialog, _ ->
                dialog.dismiss()
                Toast.makeText(this, "Come back when you're ready, $username!", Toast.LENGTH_SHORT).show()
            }
            .setCancelable(false)
            .show()
    }
}