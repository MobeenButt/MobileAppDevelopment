package com.example.lab2

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val etEmail = findViewById<TextInputEditText>(R.id.etEmail)
        val etPassword = findViewById<TextInputEditText>(R.id.etPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val tvRegisterLink = findViewById<TextView>(R.id.tvRegisterLink)

        tvRegisterLink.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        btnLogin.setOnClickListener {
            val email = etEmail.text.toString()
            val pass = etPassword.text.toString()

            if (validateLogin(etEmail, etPassword)) {
                if (checkCredentials(email, pass)) {
                    Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show()

                    // Inside LoginActivity.kt (already in your code)
                    val sharedPref = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
                    val name = sharedPref.getString("USER_NAME", "User")

                    val intent = Intent(this, Welcome::class.java)
                    intent.putExtra("USER_NAME", name) // Sending the name with the key "USER_NAME"
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Invalid Email or Password", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun validateLogin(email: TextInputEditText, pass: TextInputEditText): Boolean {
        var isValid = true
        if (!Patterns.EMAIL_ADDRESS.matcher(email.text.toString()).matches()) {
            email.error = "Invalid email address"
            isValid = false
        }
        if (pass.text.toString().isEmpty()) {
            pass.error = "Password is required"
            isValid = false
        }
        return isValid
    }

    private fun checkCredentials(email: String, pass: String): Boolean {
        val sharedPref = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val savedEmail = sharedPref.getString("USER_EMAIL", null)
        val savedPass = sharedPref.getString("USER_PASS", null)
        return email == savedEmail && pass == savedPass
    }
}