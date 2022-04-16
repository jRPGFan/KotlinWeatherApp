package com.example.kotlinweatherapp.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class HourlyWeather(
    val time: String? = "12:00",
    val temp: Double? = 0.0,
    val feelsLike: Double? = 0.0,
    val conditions: String? = "Clear",
    val icon: String? = "bkn_n"
): Parcelable

//time = hourly[i].dt (unixtime to time)
//temp = hourly[i].temp
//feelsLike = hourly[i].feels_like
//conditions = hourly[i].weather.main
//icon = hourly[i].weather.icon

fun getNextSixHours() = listOf(
    HourlyWeather("12:00", 20.0, 21.0, "clear", "bkn_n"),
    HourlyWeather("13:00", 20.0, 21.0, "clear", "bkn_n"),
    HourlyWeather("14:00", 20.0, 21.0, "clear", "bkn_n"),
    HourlyWeather("15:00", 20.0, 21.0, "clear", "bkn_n"),
    HourlyWeather("16:00", 20.0, 21.0, "clear", "bkn_n"),
    HourlyWeather("17:00", 20.0, 21.0, "clear", "bkn_n")
)
