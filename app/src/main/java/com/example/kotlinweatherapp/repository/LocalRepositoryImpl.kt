package com.example.kotlinweatherapp.repository

import com.example.kotlinweatherapp.model.Weather
import com.example.kotlinweatherapp.room.HistoryDao
import com.example.kotlinweatherapp.utilities.convertHistoryEntityToWeather
import com.example.kotlinweatherapp.utilities.convertWeatherToEntity

class LocalRepositoryImpl(
    private val localDataSource: HistoryDao
) : LocalRepository {
    override fun getAllHistory(): List<Weather> {
        return convertHistoryEntityToWeather(localDataSource.all())
    }

    override fun saveEntity(weather: Weather) {
        localDataSource.insert(convertWeatherToEntity(weather))
    }
}