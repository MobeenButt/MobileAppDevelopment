package com.example.lab2

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Patterns
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText

class RegisterActivity : AppCompatActivity() {

    private var selectedImageUri: Uri? = null
    private lateinit var ivProfilePreview: ImageView

    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            selectedImageUri = it
            ivProfilePreview.setImageURI(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        ivProfilePreview = findViewById(R.id.ivProfilePreview)
        val btnSelectImage = findViewById<Button>(R.id.btnSelectImage)
        val etFullName = findViewById<TextInputEditText>(R.id.etFullName)
        val etEmail = findViewById<TextInputEditText>(R.id.etEmail)
        val etPassword = findViewById<TextInputEditText>(R.id.etPassword)
        val rgGender = findViewById<RadioGroup>(R.id.rgGender)
        val actvRole = findViewById<AutoCompleteTextView>(R.id.actvRole)
        val cbAgreements = findViewById<CheckBox>(R.id.cbAgreements)
        val btnRegister = findViewById<Button>(R.id.btnRegister)
        val tvLoginLink = findViewById<TextView>(R.id.tvLoginLink)

        val roles = arrayOf("Student", "Employee")
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, roles)
        actvRole.setAdapter(adapter)

        btnSelectImage.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

        tvLoginLink.setOnClickListener { finish() }

        btnRegister.setOnClickListener {
            if (validateInput(etFullName, etEmail, etPassword, actvRole, cbAgreements)) {
                val selectedGender = if (rgGender.checkedRadioButtonId == R.id.rbMale) "Male" else "Female"
                
                saveUser(
                    etFullName.text.toString(),
                    etEmail.text.toString(),
                    etPassword.text.toString(),
                    selectedGender,
                    actvRole.text.toString(),
                    selectedImageUri?.toString()
                )
                
                Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun validateFullName(fullName: TextInputEditText): Boolean {
        val name = fullName.text.toString().trim()
        return if (name.isEmpty() || name.length < 3) {
            fullName.error = "Valid name required"
            false
        } else true
    }

    private fun validateEmail(email: TextInputEditText): Boolean {
        val emailText = email.text.toString().trim()
        return if (emailText.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {
            email.error = "Valid email required"
            false
        } else true
    }

    private fun validatePassword(pass: TextInputEditText): Boolean {
        val password = pass.text.toString()
        return if (password.isEmpty() || password.length < 6) {
            pass.error = "Password must be at least 6 characters"
            false
        } else true
    }

    private fun validateRole(role: AutoCompleteTextView): Boolean {
        return if (role.text.toString().isEmpty()) {
            role.error = "Role required"
            false
        } else true
    }

    private fun validateAgreements(agreements: CheckBox): Boolean {
        return if (!agreements.isChecked) {
            Toast.makeText(this, "Please agree to terms", Toast.LENGTH_SHORT).show()
            false
        } else true
    }

    private fun validateInput(
        fullName: TextInputEditText,
        email: TextInputEditText,
        pass: TextInputEditText,
        role: AutoCompleteTextView,
        agreements: CheckBox
    ): Boolean {
        return validateFullName(fullName) && validateEmail(email) && 
               validatePassword(pass) && validateRole(role) && validateAgreements(agreements)
    }

    private fun saveUser(name: String, email: String, pass: String, gender: String, role: String, imageUri: String?) {
        val sharedPref = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString("USER_NAME", name)
            putString("USER_EMAIL", email)
            putString("USER_PASS", pass)
            putString("USER_GENDER", gender)
            putString("USER_ROLE", role)
            putString("USER_IMAGE", imageUri)
            apply()
        }
    }
}