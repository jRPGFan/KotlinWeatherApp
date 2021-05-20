package com.example.kotlinweatherapp.model

import com.example.kotlinweatherapp.R

data class Weather(
    val city: City = getDefaultCity(),
    val temperature: Int = 13,
    val feelsLike: Int = 11,
    val conditions: WeatherConditions = getDefaultWeather()
)

fun getDefaultCity() = City("Кишинёв", 47.022778, 28.835278)

fun getDefaultWeather() = WeatherConditions("Rain", R.drawable.rain)