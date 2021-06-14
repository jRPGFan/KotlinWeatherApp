package com.example.kotlinweatherapp.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.time.DayOfWeek

@Parcelize
data class WeatherForAWeek(
    val dayOfWeek: DayOfWeek,
    val dayTemperature: Int = 0,
    val nightTemperature: Int = 0,
    val conditions: WeatherConditions = getRandomWeather()
) : Parcelable

fun getCityWeatherForAWeekLocalSource(): List<WeatherForAWeek> {
    return listOf(
        WeatherForAWeek(DayOfWeek.MONDAY, 25, 14),
        WeatherForAWeek(DayOfWeek.TUESDAY, 27, 17),
        WeatherForAWeek(DayOfWeek.WEDNESDAY, 22, 12),
        WeatherForAWeek(DayOfWeek.THURSDAY, 21, 16),
        WeatherForAWeek(DayOfWeek.FRIDAY, 24, 13),
        WeatherForAWeek(DayOfWeek.SATURDAY, 26, 11),
        WeatherForAWeek(DayOfWeek.SUNDAY, 29, 18),
    )
}

//fun getCityWeatherForAWeekServer(city: String): List<WeatherForAWeek> {
//    return listOf(
//        WeatherForAWeek(DayOfWeek.MONDAY, 25, 14),
//        WeatherForAWeek(DayOfWeek.TUESDAY, 25, 14),
//        WeatherForAWeek(DayOfWeek.WEDNESDAY, 25, 14),
//        WeatherForAWeek(DayOfWeek.THURSDAY, 25, 14),
//        WeatherForAWeek(DayOfWeek.FRIDAY, 25, 14),
//        WeatherForAWeek(DayOfWeek.SATURDAY, 25, 14),
//        WeatherForAWeek(DayOfWeek.SUNDAY, 25, 14),
//    )
//}