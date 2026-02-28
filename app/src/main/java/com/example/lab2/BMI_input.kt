package com.example.lab2

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class BmiInputActivity : AppCompatActivity() {

    private lateinit var tilName: TextInputLayout
    private lateinit var tilAge: TextInputLayout
    private lateinit var tilHeight: TextInputLayout
    private lateinit var tilWeight: TextInputLayout

    private lateinit var etName: TextInputEditText
    private lateinit var etAge: TextInputEditText
    private lateinit var etHeight: TextInputEditText
    private lateinit var etWeight: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bmi_input)

        tilName   = findViewById(R.id.tilName)
        tilAge    = findViewById(R.id.tilAge)
        tilHeight = findViewById(R.id.tilHeight)
        tilWeight = findViewById(R.id.tilWeight)

        etName   = findViewById(R.id.etName)
        etAge    = findViewById(R.id.etAge)
        etHeight = findViewById(R.id.etHeight)
        etWeight = findViewById(R.id.etWeight)

        findViewById<android.widget.Button>(R.id.btnCalculate).setOnClickListener {
            if (validateInputs()) {
                navigateToResult()
            }
        }
    }

    private fun validateInputs(): Boolean {
        var isValid = true

        val name   = etName.text.toString().trim()
        val age    = etAge.text.toString().trim()
        val height = etHeight.text.toString().trim()
        val weight = etWeight.text.toString().trim()

        // Clear previous errors
        tilName.error   = null
        tilAge.error    = null
        tilHeight.error = null
        tilWeight.error = null

        if (name.isEmpty()) {
            tilName.error = "Please enter your name"
            isValid = false
        }

        if (age.isEmpty()) {
            tilAge.error = "Please enter your age"
            isValid = false
        } else if (age.toIntOrNull() == null || age.toInt() <= 0 || age.toInt() > 120) {
            tilAge.error = "Please enter a valid age (1–120)"
            isValid = false
        }

        if (height.isEmpty()) {
            tilHeight.error = "Please enter your height"
            isValid = false
        } else if (height.toDoubleOrNull() == null || height.toDouble() <= 0) {
            tilHeight.error = "Please enter a valid height"
            isValid = false
        }

        if (weight.isEmpty()) {
            tilWeight.error = "Please enter your weight"
            isValid = false
        } else if (weight.toDoubleOrNull() == null || weight.toDouble() <= 0) {
            tilWeight.error = "Please enter a valid weight"
            isValid = false
        }

        return isValid
    }

    private fun navigateToResult() {
        val intent = Intent(this, BmiResultActivity::class.java).apply {
            putExtra("NAME",   etName.text.toString().trim())
            putExtra("AGE",    etAge.text.toString().trim())
            putExtra("HEIGHT", etHeight.text.toString().trim().toDouble())
            putExtra("WEIGHT", etWeight.text.toString().trim().toDouble())
        }
        startActivity(intent)
    }
}
