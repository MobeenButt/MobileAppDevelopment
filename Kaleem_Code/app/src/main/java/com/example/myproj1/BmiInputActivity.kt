package com.example.myproj1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class BmiInputActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_bmi_input)
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val etName = findViewById<EditText>(R.id.etBmiName)
        val etAge = findViewById<EditText>(R.id.etBmiAge)
        val etHeight = findViewById<EditText>(R.id.etBmiHeight)
        val etWeight = findViewById<EditText>(R.id.etBmiWeight)
        val btnCalculate = findViewById<Button>(R.id.btnCalculateBmi)

        btnCalculate.setOnClickListener {
            val name = etName.text.toString().trim()
            val age = etAge.text.toString().trim()
            val heightStr = etHeight.text.toString().trim()
            val weightStr = etWeight.text.toString().trim()

            if (name.isEmpty() || age.isEmpty() || heightStr.isEmpty() || weightStr.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val intent = Intent(this, BmiResultActivity::class.java).apply {
                putExtra("USER_NAME", name)
                putExtra("USER_AGE", age)
                putExtra("USER_HEIGHT", heightStr.toDouble())
                putExtra("USER_WEIGHT", weightStr.toDouble())
            }
            startActivity(intent)
        }
    }
}
