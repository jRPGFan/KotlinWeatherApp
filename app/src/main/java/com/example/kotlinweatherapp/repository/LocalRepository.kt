package com.example.kotlinweatherapp.repository

import com.example.kotlinweatherapp.model.Weather

interface LocalRepository {
    fun getAllHistory(): List<Weather>
    fun saveEntity(weather: Weather)
}