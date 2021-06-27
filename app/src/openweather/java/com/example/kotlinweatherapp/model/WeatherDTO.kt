package com.example.kotlinweatherapp.model

data class WeatherDTO(
    val current: CurrentDTO?,
    val hourly: List<HourlyDTO?>,
    val daily: List<DailyDTO?>
)

data class CurrentDTO(
    val temp: Double?,
    val feels_like: Double?,
    val weather: List<ConditionDTO?>
)

data class ConditionDTO(
    val id: Int?,
    val main: String?,
    val description: String?,
    val icon: String?
)

data class HourlyDTO(
    val dt: Int?,
    val temp: Double?,
    val feels_like: Double?,
    val weather: List<ConditionDTO?>
)

data class DailyDTO(
    val dt: Int?,
    val temp: DailyTempDTO?,
    val feels_like: DailyFeelsLikeDTO?,
    val weather: List<ConditionDTO?>
)

data class DailyTempDTO(
    val day: Double?,
    val night: Double?
)

data class DailyFeelsLikeDTO(
    val day: Double?,
    val night: Double?
)
