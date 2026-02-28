package com.example.myproj1

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.Locale

class Task : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_task)
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val etPrice = findViewById<EditText>(R.id.etPrice)
        val btnCalculate = findViewById<Button>(R.id.btnCalculate)
        val tvDiscountAmount = findViewById<TextView>(R.id.tvDiscountAmount)
        val tvFinalPrice = findViewById<TextView>(R.id.tvFinalPrice)

        btnCalculate.setOnClickListener {
            val priceString = etPrice.text.toString()
            if (priceString.isNotEmpty()) {
                val price = priceString.toDouble()
                val discount = price * 0.20
                val finalPrice = price - discount

                val formattedDiscount = String.format(Locale.getDefault(), "%.2f", discount)
                val formattedFinalPrice = String.format(Locale.getDefault(), "%.2f", finalPrice)

                tvDiscountAmount.text = getString(R.string.discount_amount, formattedDiscount)
                tvFinalPrice.text = getString(R.string.final_price, formattedFinalPrice)
            } else {
                Toast.makeText(this, getString(R.string.error_empty_price), Toast.LENGTH_SHORT).show()
            }
        }
    }
}
