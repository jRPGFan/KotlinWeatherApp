package com.example.kotlinweatherapp.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.kotlinweatherapp.R
import com.example.kotlinweatherapp.databinding.MainActivityBinding
import com.example.kotlinweatherapp.view.main.MainFragment

class MainActivity : AppCompatActivity() {
    private val binding: MainActivityBinding by lazy {
        MainActivityBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commit()
        }
    }
}