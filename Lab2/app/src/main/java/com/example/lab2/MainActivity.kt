package com.example.lab2

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val listView = ListView(this)
        setContentView(listView)
//for new kt add here
        val screenNames = arrayOf(
            "Login Screen",
            "Register Screen",
            "BMI Calculator",
            "Calculator",
            "Welcome Screen",
            "Quiz"
        )

        //also add name of file :: class.java here
        @Suppress("UNCHECKED_CAST")
        val classes = arrayOf(
            LoginActivity::class.java,
            RegisterActivity::class.java,
            BmiInputActivity::class.java,
            Calculator::class.java,
            Welcome::class.java,
            Quiz::class.java
        )

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, screenNames)
        listView.adapter = adapter

        listView.setOnItemClickListener { _, _, position, _ ->
            val intent = Intent(this, classes[position])
            startActivity(intent)
        }
    }
}
