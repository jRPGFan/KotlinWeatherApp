package com.example.kotlinweatherapp.model

class RepositoryImpl : Repository {
    override fun getWeatherFromServer() =  Weather()

    override fun getWeatherFromLocalStorageRus() = getRussianCities()

    override fun getWeatherFromLocalStorageWorld() = getWorldCities()

    override fun getCityWeatherForAWeekFromLocalStorage() = getCityWeatherForAWeekLocalSource()

    override fun getCityWeatherForAWeekFromServer(city: String) = getCityWeatherForAWeekServer(city)
}