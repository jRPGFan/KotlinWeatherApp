package com.example.kotlinweatherapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val txtClicks = findViewById<TextView>(R.id.clickCount);
        var clicks: Int = 0;
        val button = findViewById<Button>(R.id.click)
        button.setOnClickListener{
           clicks++
           txtClicks.text = clicks.toString()
        }
    }
}