package com.example.kotlinweatherapp.model

interface Repository {
    fun getWeatherFromServer(): Weather
    fun getWeatherFromLocalStorageRus(): List<Weather>
    fun getWeatherFromLocalStorageWorld(): List<Weather>
    fun getCityWeatherForAWeekFromLocalStorage() : List<WeatherForAWeek>
    fun getCityWeatherForAWeekFromServer(city: String) : List<WeatherForAWeek>
}