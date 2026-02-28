package com.example.lab2

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Welcome : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_welcome)
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val ivProfileWelcome = findViewById<ImageView>(R.id.ivProfileWelcome)
        val tvWelcomeMessage = findViewById<TextView>(R.id.tvWelcomeMessage)
        
        val sharedPref = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val userName = sharedPref.getString("USER_NAME", "Guest")
        val imageUriString = sharedPref.getString("USER_IMAGE", null)

        tvWelcomeMessage.text = "Welcome, $userName!"
        
        imageUriString?.let {
            try {
                val uri = Uri.parse(it)
                ivProfileWelcome.setImageURI(uri)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}