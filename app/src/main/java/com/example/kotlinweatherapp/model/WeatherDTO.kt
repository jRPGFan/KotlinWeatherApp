package com.example.kotlinweatherapp.model

data class WeatherDTO(
    val fact: FactDTO?,
    val forecast: ForecastDTO?
)

data class FactDTO(
    val temp: Int?,
    val feels_like: Int?,
    val condition: String?
)

data class ForecastDTO(
    val date: String?,
    val parts: List<Parts>
)

data class Parts(
    val temp_max: Int?,
    val temp_min: Int?,
    val condition: String?
)