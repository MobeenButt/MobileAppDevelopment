package com.example.lab2

import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import java.util.Locale

class BmiResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bmi_result)

        // Retrieve passed data
        val name      = intent.getStringExtra("NAME") ?: ""
        val age       = intent.getStringExtra("AGE") ?: ""
        val heightCm  = intent.getDoubleExtra("HEIGHT", 0.0)
        val weightKg  = intent.getDoubleExtra("WEIGHT", 0.0)

        // Calculate BMI
        val heightM = heightCm / 100.0
        val bmi     = weightKg / (heightM * heightM)

        // Bind user details
        findViewById<TextView>(R.id.tvResultName).text   = name
        findViewById<TextView>(R.id.tvResultAge).text    = "$age years"
        findViewById<TextView>(R.id.tvResultHeight).text =
            String.format(Locale.getDefault(), "%.1f cm  /  %.2f m", heightCm, heightM)
        findViewById<TextView>(R.id.tvResultWeight).text =
            String.format(Locale.getDefault(), "%.1f kg", weightKg)

        // Bind BMI value
        val tvBmiValue       = findViewById<TextView>(R.id.tvBmiValue)
        val tvBmiCategory    = findViewById<TextView>(R.id.tvBmiCategory)
        val tvBmiDescription = findViewById<TextView>(R.id.tvBmiDescription)
        val cardBmiResult    = findViewById<CardView>(R.id.cardBmiResult)

        tvBmiValue.text = String.format(Locale.getDefault(), "%.1f", bmi)

        // Determine category and color
        when {
            bmi < 18.5 -> {
                tvBmiCategory.text    = "Underweight"
                tvBmiDescription.text = "Your BMI is below the healthy range.\nConsider consulting a nutritionist."
                cardBmiResult.setCardBackgroundColor(Color.parseColor("#1565C0")) // Blue
            }
            bmi <= 24.9 -> {
                tvBmiCategory.text    = "Normal"
                tvBmiDescription.text = "Your BMI is in the healthy range.\nKeep maintaining your lifestyle!"
                cardBmiResult.setCardBackgroundColor(Color.parseColor("#2E7D32")) // Green
            }
            else -> {
                tvBmiCategory.text    = "Overweight"
                tvBmiDescription.text = "Your BMI is above the healthy range.\nConsider a balanced diet and exercise."
                cardBmiResult.setCardBackgroundColor(Color.parseColor("#C62828")) // Red
            }
        }

        // Recalculate button
        findViewById<android.widget.Button>(R.id.btnRecalculate).setOnClickListener {
            finish() // Go back to Screen 1
        }
    }
}