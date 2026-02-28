package com.example.myproj1

import android.os.Bundle
import android.util.Patterns
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class RegisterPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register_page)
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val etName = findViewById<EditText>(R.id.etName)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val etConfirmPassword = findViewById<EditText>(R.id.etConfirmPassword)
        val rgGender = findViewById<RadioGroup>(R.id.rgGender)
        val spinnerCity = findViewById<Spinner>(R.id.spinnerCity)
        val cbJava = findViewById<CheckBox>(R.id.cbJava)
        val cbKotlin = findViewById<CheckBox>(R.id.cbKotlin)
        val cbAndroid = findViewById<CheckBox>(R.id.cbAndroid)
        val cbAgree = findViewById<CheckBox>(R.id.cbAgree)
        val btnRegister = findViewById<Button>(R.id.btnRegister)
        val tvLoginLink = findViewById<TextView>(R.id.tvLoginLink)

        // Set up Spinner (City)
        val cities = arrayOf("Karachi", "Lahore", "Islamabad", "Faisalabad", "Multan")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, cities)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCity.adapter = adapter

        btnRegister.setOnClickListener {
            val name = etName.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()
            val confirmPassword = etConfirmPassword.text.toString().trim()
            
            // Validation
            if (name.isEmpty()) {
                etName.error = "Name is required"
                etName.requestFocus()
                return@setOnClickListener
            }

            if (email.isEmpty()) {
                etEmail.error = "Email is required"
                etEmail.requestFocus()
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                etEmail.error = "Please enter a valid email"
                etEmail.requestFocus()
                return@setOnClickListener
            }

            // Get selected Gender
            val selectedGenderId = rgGender.checkedRadioButtonId
            if (selectedGenderId == -1) {
                Toast.makeText(this, "Please select gender", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val gender = findViewById<RadioButton>(selectedGenderId).text.toString()

            val city = spinnerCity.selectedItem.toString()

            // Get Skills in Array/List
            val selectedSkills = mutableListOf<String>()
            if (cbJava.isChecked) selectedSkills.add("Java")
            if (cbKotlin.isChecked) selectedSkills.add("Kotlin")
            if (cbAndroid.isChecked) selectedSkills.add("Android")

            if (selectedSkills.isEmpty()) {
                Toast.makeText(this, "Please select at least one skill", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!cbAgree.isChecked) {
                Toast.makeText(this, "Please agree to terms and conditions", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                etPassword.error = "Password is required"
                etPassword.requestFocus()
                return@setOnClickListener
            }

            if (password.length < 6) {
                etPassword.error = "Password must be at least 6 characters"
                etPassword.requestFocus()
                return@setOnClickListener
            }

            if (confirmPassword != password) {
                etConfirmPassword.error = "Passwords do not match"
                etConfirmPassword.requestFocus()
                return@setOnClickListener
            }

            // If all validations pass
            val successMsg = "Registration Successful!\nName: $name\nSkills: ${selectedSkills.joinToString(", ")}"
            Toast.makeText(this, successMsg, Toast.LENGTH_LONG).show()
            
            // Navigate back or to next screen
            // finish()
        }

        tvLoginLink.setOnClickListener {
            finish()
        }
    }
}
