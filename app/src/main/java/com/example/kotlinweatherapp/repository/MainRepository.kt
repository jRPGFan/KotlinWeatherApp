package com.example.kotlinweatherapp.repository

import com.example.kotlinweatherapp.model.Weather
import com.example.kotlinweatherapp.model.WeatherForAWeek

interface MainRepository {
    fun getWeatherFromServer(): Weather
    fun getWeatherFromLocalStorageRus(): List<Weather>
    fun getWeatherFromLocalStorageWorld(): List<Weather>
    fun getCityWeatherForAWeekFromLocalStorage() : List<WeatherForAWeek>
    fun getCityWeatherForAWeekFromServer(city: String) : List<WeatherForAWeek>
}