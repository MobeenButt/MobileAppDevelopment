package com.example.myproj1

import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.Locale

class BmiResultActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_bmi_result)
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val name = intent.getStringExtra("USER_NAME")
        val age = intent.getStringExtra("USER_AGE")
        val height = intent.getDoubleExtra("USER_HEIGHT", 0.0)
        val weight = intent.getDoubleExtra("USER_WEIGHT", 0.0)

        findViewById<TextView>(R.id.tvResName).text = "Name: $name"
        findViewById<TextView>(R.id.tvResAge).text = "Age: $age"
        findViewById<TextView>(R.id.tvResHeight).text = "Height: $height cm"
        findViewById<TextView>(R.id.tvResWeight).text = "Weight: $weight kg"

        val heightInMeters = height / 100
        val bmi = weight / (heightInMeters * heightInMeters)

        val tvBmiScore = findViewById<TextView>(R.id.tvBmiScore)
        val tvBmiStatus = findViewById<TextView>(R.id.tvBmiStatus)

        tvBmiScore.text = String.format(Locale.getDefault(), "%.1f", bmi)

        when {
            bmi < 18.5 -> {
                tvBmiStatus.text = "Underweight"
                tvBmiStatus.setTextColor(Color.BLUE)
            }
            bmi in 18.5..24.9 -> {
                tvBmiStatus.text = "Normal"
                tvBmiStatus.setTextColor(Color.GREEN)
            }
            else -> {
                tvBmiStatus.text = "Overweight"
                tvBmiStatus.setTextColor(Color.RED)
            }
        }

        findViewById<Button>(R.id.btnRecalculate).setOnClickListener {
            finish()
        }
    }
}
