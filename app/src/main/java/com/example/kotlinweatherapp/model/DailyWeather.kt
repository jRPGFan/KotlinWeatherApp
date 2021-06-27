package com.example.kotlinweatherapp.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.time.DayOfWeek

@Parcelize
data class DailyWeather (
    val dayOfWeek: String?,
    val dayTemp: Double?,
    val nightTemp: Double?,
    val feelsLikeDay: Double?,
    val feelsLikeNight: Double?,
    val conditions: String?,
    val icon: String?
) : Parcelable

//dayOfWeek = daily[i].date (unixtime to dayOfWeek)
//dailyTemp[0] = daily[i].temp.day
//dailyTemp[1] = daily[i].temp.night
//feelsLikeDaily[0] = daily[i].feels_like.day
//feelsLikeDaily[1] = daily[i].feels_like.night
//conditions = daily[i].weather.main
//icon = daily[i].weather.icon

fun getWeatherForAWeek() = listOf(
    DailyWeather("Monday", 20.0, 10.0, 20.0, 10.0, "clear", "bkn_n"),
    DailyWeather("Tuesday", 20.0, 10.0, 20.0, 10.0, "clear", "bkn_n"),
    DailyWeather("Wednesday", 20.0, 10.0, 20.0, 10.0, "clear", "bkn_n"),
    DailyWeather("Thursday", 20.0, 10.0, 20.0, 10.0, "clear", "bkn_n"),
    DailyWeather("Friday", 20.0, 10.0, 20.0, 10.0, "clear", "bkn_n"),
    DailyWeather("Saturday", 20.0, 10.0, 20.0, 10.0, "clear", "bkn_n"),
    DailyWeather("Sunday", 20.0, 10.0, 20.0, 10.0, "clear", "bkn_n")
)