package com.example.kotlinweatherapp.model

class RepositoryImpl : Repository {
    override fun getWeatherFromServer(): Weather {
        return Weather()
    }

    override fun getWeatherFromLocalStorageRus(): List<Weather> {
        return getRussianCities()
    }

    override fun getWeatherFromLocalStorageWorld(): List<Weather> {
        return getWorldCities()
    }

    override fun getCityWeatherForAWeekFromLocalStorage(): List<WeatherForAWeek> {
        return getCityWeatherForAWeekLocalSource()
    }

    override fun getCityWeatherForAWeekFromServer(city: String): List<WeatherForAWeek> {
        return getCityWeatherForAWeekServer(city)
    }
}