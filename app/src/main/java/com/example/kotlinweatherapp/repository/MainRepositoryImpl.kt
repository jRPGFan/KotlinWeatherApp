package com.example.kotlinweatherapp.repository

import com.example.kotlinweatherapp.model.*

class MainRepositoryImpl : MainRepository {
    override fun getWeatherFromServer() = Weather()

    override fun getWeatherFromLocalStorageRus() = getRussianCities()

    override fun getWeatherFromLocalStorageWorld() = getWorldCities()

    override fun getCityWeatherForAWeekFromLocalStorage() = getCityWeatherForAWeekLocalSource()

//    override fun getCityWeatherForAWeekFromServer(city: String) = getCityWeatherForAWeekServer(city)
}