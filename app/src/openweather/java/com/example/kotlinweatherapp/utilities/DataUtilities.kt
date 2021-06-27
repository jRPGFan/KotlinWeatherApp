package com.example.kotlinweatherapp.utilities

import android.annotation.SuppressLint
import com.example.kotlinweatherapp.model.*
import com.example.kotlinweatherapp.room.HistoryEntity
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.math.roundToInt

fun convertDtoToModel(weatherDTO: WeatherDTO): Weather {
    val current: CurrentDTO? = weatherDTO.current
    val hourly: List<HourlyDTO?> = weatherDTO.hourly
    val daily: List<DailyDTO?> = weatherDTO.daily

    return Weather(
        getDefaultCity(),
        current?.temp!!,
        current.feels_like!!,
        current.weather[0]?.main!!,
        current.weather[0]?.icon,
        convertHourlyDtoToModel(hourly),
        convertDailyDtoToModel(daily)
    )
}

fun convertHourlyDtoToModel(hourly: List<HourlyDTO?>): List<HourlyWeather> {
    val hourlyList = mutableListOf<HourlyWeather>()
    for (i in 0..5) {
        hourlyList.add(
            HourlyWeather(
                getTimeFromUnixTime(hourly[i]?.dt),
                hourly[i]?.temp,
                hourly[i]?.feels_like,
                hourly[i]?.weather?.get(0)?.main,
                hourly[i]?.weather?.get(0)?.icon
            )
        )
    }

    return hourlyList
}

fun convertDailyDtoToModel(daily: List<DailyDTO?>): List<DailyWeather> {
    val dailyList = mutableListOf<DailyWeather>()
    for (day in daily) {
        dailyList.add(
            DailyWeather(
                getDayOfWeekFromUnixTime(day?.dt),
                day?.temp?.day,
                day?.temp?.night,
                day?.feels_like?.day,
                day?.feels_like?.night,
                day?.weather?.get(0)?.main,
                day?.weather?.get(0)?.icon
            )
        )
    }

    return dailyList
}

@SuppressLint("SimpleDateFormat")
fun getTimeFromUnixTime(unixTime: Int?): String? {
    return try {
        val sdf = SimpleDateFormat("HH:mm:ss")
        val netDate = unixTime?.toLong()?.times(1000)?.let { Date(it) }
        sdf.format(netDate)
    } catch (e: Exception) {
        e.toString()
    }
}

@SuppressLint("SimpleDateFormat")
fun getDayOfWeekFromUnixTime(unixTime: Int?): String? {
    return try {
        val sdf = SimpleDateFormat("E")
        val netDate = unixTime?.toLong()?.times(1000)?.let { Date(it) }
        sdf.format(netDate)
    } catch (e: Exception) {
        e.toString()
    }
}

fun convertHistoryEntityToWeather(entityList: List<HistoryEntity>): List<Weather> {
    return entityList.map {
        Weather(City(it.city, 0.0, 0.0), it.temperature.toDouble(), 0.0, it.condition)
    }
}

fun convertWeatherToEntity(weather: Weather): HistoryEntity {
    return HistoryEntity(0, weather.city.city, weather.temperature.roundToInt(), weather.conditions)
}

fun convertCelsiusToFahrenheit(temperature: Double): Double {
    return temperature * 9 / 5 + 32
}