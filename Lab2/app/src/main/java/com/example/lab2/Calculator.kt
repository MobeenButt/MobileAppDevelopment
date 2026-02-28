package com.example.lab2

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textfield.TextInputEditText
import java.util.Locale

class Calculator : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_calculator)
        
        val mainView = findViewById<View>(R.id.main)
        ViewCompat.setOnApplyWindowInsetsListener(mainView) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val etPrice = findViewById<TextInputEditText>(R.id.etPrice)
        val btnCalculate = findViewById<Button>(R.id.btnCalculate)
        val resultCard = findViewById<MaterialCardView>(R.id.resultCard)
        val tvDiscountAmount = findViewById<TextView>(R.id.tvDiscountAmount)
        val tvFinalPrice = findViewById<TextView>(R.id.tvFinalPrice)

        btnCalculate.setOnClickListener {
            val priceString = etPrice.text.toString()
            
            if (priceString.isNotEmpty()) {
                val originalPrice = priceString.toDoubleOrNull() ?: 0.0
                val discountPercent = 20.0
                
                val discountAmount = (originalPrice * discountPercent) / 100
                val finalPrice = originalPrice - discountAmount

                tvDiscountAmount.text = String.format(Locale.getDefault(), "-$%.2f", discountAmount)
                tvFinalPrice.text = String.format(Locale.getDefault(), "$%.2f", finalPrice)
                
                resultCard.visibility = View.VISIBLE
            } else {
                etPrice.error = "Please enter a price"
                resultCard.visibility = View.GONE
            }
        }
    }
}
