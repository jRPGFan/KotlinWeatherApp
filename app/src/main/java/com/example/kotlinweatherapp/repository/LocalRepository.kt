package com.example.kotlinweatherapp.repository

import com.example.kotlinweatherapp.model.Weather

interface LocalRepository {
    fun getAllHistory(): List<Weather>
    fun getHistoryByCity(city: String): List<Weather>
    fun saveEntity(weather: Weather)
}